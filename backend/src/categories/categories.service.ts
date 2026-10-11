import {
  BadRequestException,
  ConflictException,
  Injectable,
  NotFoundException,
} from '@nestjs/common';
import { InjectDataSource, InjectRepository } from '@nestjs/typeorm';
import { DataSource, EntityManager, Repository } from 'typeorm';
import { round2 } from '../common/utils/money';
import { AccountsService } from '../accounts/accounts.service';
import { Transaction } from '../transactions/entities/transaction.entity';
import { GroupTransaction } from '../group-transactions/entities/group-transaction.entity';
import { Category } from './entities/category.entity';
import { CreateCategoryDto } from './dto/create-category.dto';
import { UpdateCategoryDto } from './dto/update-category.dto';

@Injectable()
export class CategoriesService {
  constructor(
    @InjectRepository(Category)
    private readonly categoryRepo: Repository<Category>,
    @InjectDataSource() private readonly dataSource: DataSource,
    private readonly accountsService: AccountsService,
  ) {}

  async create(userId: string, dto: CreateCategoryDto): Promise<Category> {
    const account = await this.accountsService.findByUserId(userId);
    return this.categoryRepo.save(
      this.categoryRepo.create({
        ...dto,
        accountId: account.accountId,
        balance: 0,
      }),
    );
  }

  async findAll(userId: string): Promise<Category[]> {
    const account = await this.accountsService.findByUserId(userId);
    return this.categoryRepo.find({
      where: { accountId: account.accountId },
      order: { name: 'ASC' },
    });
  }

  async findOne(userId: string, categoryId: string): Promise<Category> {
    const account = await this.accountsService.findByUserId(userId);
    const category = await this.categoryRepo.findOneBy({
      categoryId,
      accountId: account.accountId,
    });
    if (!category) {
      throw new NotFoundException('Kategori tidak ditemukan');
    }
    return category;
  }

  async update(
    userId: string,
    categoryId: string,
    dto: UpdateCategoryDto,
  ): Promise<Category> {
    const category = await this.findOne(userId, categoryId);
    return this.categoryRepo.save(this.categoryRepo.merge(category, dto));
  }

  /** Hapus kategori; sisa saldonya dikembalikan ke main account. */
  async remove(userId: string, categoryId: string): Promise<void> {
    await this.dataSource.transaction(async (manager) => {
      const account = await this.accountsService.findByUserId(userId, manager);
      const category = await this.lockOwned(manager, userId, categoryId);

      const used =
        (await manager.existsBy(Transaction, { categoryId })) ||
        (await manager.existsBy(GroupTransaction, { categoryId }));
      if (used) {
        throw new ConflictException(
          'Kategori masih dipakai oleh transaksi, hapus/pindahkan transaksinya dulu',
        );
      }

      account.balance = round2(account.balance + category.balance);
      await manager.save(account);
      await manager.remove(category);
    });
  }

  /** Pindahkan dana dari main account ke kategori. */
  allocate(
    userId: string,
    categoryId: string,
    amount: number,
  ): Promise<Category> {
    return this.dataSource.transaction(async (manager) => {
      const account = await this.accountsService.findByUserId(userId, manager);
      if (account.balance < amount) {
        throw new BadRequestException('Saldo main account tidak cukup');
      }
      const category = await this.lockOwned(manager, userId, categoryId);
      account.balance = round2(account.balance - amount);
      category.balance = round2(category.balance + amount);
      await manager.save(account);
      return manager.save(category);
    });
  }

  /** Kembalikan dana dari kategori ke main account. */
  release(
    userId: string,
    categoryId: string,
    amount: number,
  ): Promise<Category> {
    return this.dataSource.transaction(async (manager) => {
      const account = await this.accountsService.findByUserId(userId, manager);
      const category = await this.lockOwned(manager, userId, categoryId);
      if (category.balance < amount) {
        throw new BadRequestException('Saldo kategori tidak cukup');
      }
      category.balance = round2(category.balance - amount);
      account.balance = round2(account.balance + amount);
      await manager.save(account);
      return manager.save(category);
    });
  }

  /**
   * Ambil & kunci kategori milik user di dalam transaksi DB.
   * Dipakai oleh service lain yang mengubah saldo kategori.
   */
  async lockOwned(
    manager: EntityManager,
    userId: string,
    categoryId: string,
  ): Promise<Category> {
    const account = await this.accountsService.findByUserId(userId, manager);
    const category = await manager.findOne(Category, {
      where: { categoryId, accountId: account.accountId },
      lock: { mode: 'pessimistic_write' },
    });
    if (!category) {
      throw new NotFoundException('Kategori tidak ditemukan');
    }
    return category;
  }
}
