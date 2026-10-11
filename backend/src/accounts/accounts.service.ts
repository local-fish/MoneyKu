import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectDataSource, InjectRepository } from '@nestjs/typeorm';
import { DataSource, EntityManager, Repository } from 'typeorm';
import { round2 } from '../common/utils/money';
import { Category } from '../categories/entities/category.entity';
import { MainAccount } from './entities/main-account.entity';

@Injectable()
export class AccountsService {
  constructor(
    @InjectRepository(MainAccount)
    private readonly accountRepo: Repository<MainAccount>,
    @InjectRepository(Category)
    private readonly categoryRepo: Repository<Category>,
    @InjectDataSource() private readonly dataSource: DataSource,
  ) {}

  /**
   * Ambil main account milik user. Jika `manager` diberikan, baris akan
   * dikunci (SELECT ... FOR UPDATE) di dalam transaksi DB tersebut.
   */
  async findByUserId(
    userId: string,
    manager?: EntityManager,
  ): Promise<MainAccount> {
    const account = manager
      ? await manager.findOne(MainAccount, {
          where: { userId },
          lock: { mode: 'pessimistic_write' },
        })
      : await this.accountRepo.findOneBy({ userId });
    if (!account) {
      throw new NotFoundException('Main account tidak ditemukan');
    }
    return account;
  }

  /** Saldo main account + total budget (SUM saldo kategori) via query. */
  async getSummary(userId: string) {
    const account = await this.findByUserId(userId);
    const raw = await this.categoryRepo
      .createQueryBuilder('category')
      .select('COALESCE(SUM(category.balance), 0)', 'totalBudget')
      .addSelect('COUNT(category.category_id)', 'categoryCount')
      .where('category.account_id = :accountId', {
        accountId: account.accountId,
      })
      .getRawOne<{ totalBudget: string; categoryCount: string }>();

    const totalBudget = Number(raw?.totalBudget ?? 0);
    return {
      ...account,
      totalBudget,
      categoryCount: Number(raw?.categoryCount ?? 0),
      totalBalance: round2(account.balance + totalBudget),
    };
  }

  async deposit(userId: string, amount: number): Promise<MainAccount> {
    return this.dataSource.transaction(async (manager) => {
      const account = await this.findByUserId(userId, manager);
      account.balance = round2(account.balance + amount);
      return manager.save(account);
    });
  }
}
