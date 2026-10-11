import { Injectable, NotFoundException } from '@nestjs/common';
import { EventEmitter2 } from '@nestjs/event-emitter';
import { InjectDataSource, InjectRepository } from '@nestjs/typeorm';
import {
  Between,
  DataSource,
  FindOptionsWhere,
  LessThanOrEqual,
  MoreThanOrEqual,
  Repository,
} from 'typeorm';
import { round2 } from '../common/utils/money';
import { CategoriesService } from '../categories/categories.service';
import { Category } from '../categories/entities/category.entity';
import {
  AppEvents,
  CategoryLowBalanceEvent,
  TransactionCreatedEvent,
} from '../notifications/notification.events';
import { Transaction, TransactionType } from './entities/transaction.entity';
import { CreateTransactionDto } from './dto/create-transaction.dto';
import { UpdateTransactionDto } from './dto/update-transaction.dto';
import { QueryTransactionDto } from './dto/query-transaction.dto';

/** Efek transaksi ke saldo kategori: Income (+), Expense (-). */
const balanceEffect = (type: TransactionType, amount: number): number =>
  type === TransactionType.INCOME ? amount : -amount;

@Injectable()
export class TransactionsService {
  constructor(
    @InjectRepository(Transaction)
    private readonly transactionRepo: Repository<Transaction>,
    @InjectDataSource() private readonly dataSource: DataSource,
    private readonly categoriesService: CategoriesService,
    private readonly eventEmitter: EventEmitter2,
  ) {}

  async create(
    userId: string,
    dto: CreateTransactionDto,
  ): Promise<Transaction> {
    const { transactionDate, ...rest } = dto;

    const { transaction, category } = await this.dataSource.transaction(
      async (manager) => {
        const category = await this.categoriesService.lockOwned(
          manager,
          userId,
          dto.categoryId,
        );
        category.balance = round2(
          category.balance + balanceEffect(dto.type, dto.amount),
        );
        await manager.save(category);

        const transaction = await manager.save(
          manager.create(Transaction, {
            ...rest,
            userId,
            transactionDate: transactionDate
              ? new Date(transactionDate)
              : new Date(),
          }),
        );
        return { transaction, category };
      },
    );

    this.eventEmitter.emit(
      AppEvents.TRANSACTION_CREATED,
      new TransactionCreatedEvent(userId, transaction),
    );
    this.emitIfLowBalance(userId, category);
    return transaction;
  }

  async findAll(userId: string, query: QueryTransactionDto) {
    const { type, categoryId, startDate, endDate, page, limit } = query;

    const where: FindOptionsWhere<Transaction> = { userId };
    if (type) where.type = type;
    if (categoryId) where.categoryId = categoryId;
    if (startDate && endDate) {
      where.transactionDate = Between(new Date(startDate), new Date(endDate));
    } else if (startDate) {
      where.transactionDate = MoreThanOrEqual(new Date(startDate));
    } else if (endDate) {
      where.transactionDate = LessThanOrEqual(new Date(endDate));
    }

    const [data, total] = await this.transactionRepo.findAndCount({
      where,
      relations: { category: true },
      order: { transactionDate: 'DESC' },
      skip: (page - 1) * limit,
      take: limit,
    });

    return {
      data,
      meta: { page, limit, total, totalPages: Math.ceil(total / limit) },
    };
  }

  async findOne(userId: string, transactionId: string): Promise<Transaction> {
    const transaction = await this.transactionRepo.findOne({
      where: { transactionId, userId },
      relations: { category: true },
    });
    if (!transaction) {
      throw new NotFoundException('Transaksi tidak ditemukan');
    }
    return transaction;
  }

  /** Update transaksi: batalkan efek saldo lama, lalu terapkan efek baru. */
  async update(
    userId: string,
    transactionId: string,
    dto: UpdateTransactionDto,
  ): Promise<Transaction> {
    const { transactionDate, ...rest } = dto;

    const categories = await this.dataSource.transaction(async (manager) => {
      const existing = await manager.findOne(Transaction, {
        where: { transactionId, userId },
        lock: { mode: 'pessimistic_write' },
      });
      if (!existing) {
        throw new NotFoundException('Transaksi tidak ditemukan');
      }

      const oldCategory = await this.categoriesService.lockOwned(
        manager,
        userId,
        existing.categoryId,
      );
      oldCategory.balance = round2(
        oldCategory.balance - balanceEffect(existing.type, existing.amount),
      );

      manager.merge(Transaction, existing, rest);
      if (transactionDate) {
        existing.transactionDate = new Date(transactionDate);
      }

      const newCategory =
        existing.categoryId === oldCategory.categoryId
          ? oldCategory
          : await this.categoriesService.lockOwned(
              manager,
              userId,
              existing.categoryId,
            );
      newCategory.balance = round2(
        newCategory.balance + balanceEffect(existing.type, existing.amount),
      );

      const touched = [...new Set([oldCategory, newCategory])];
      await manager.save(touched);
      await manager.save(existing);
      return touched;
    });

    categories.forEach((category) => this.emitIfLowBalance(userId, category));
    return this.findOne(userId, transactionId);
  }

  /** Hapus transaksi dan batalkan efeknya ke saldo kategori. */
  async remove(userId: string, transactionId: string): Promise<void> {
    await this.dataSource.transaction(async (manager) => {
      const existing = await manager.findOne(Transaction, {
        where: { transactionId, userId },
        lock: { mode: 'pessimistic_write' },
      });
      if (!existing) {
        throw new NotFoundException('Transaksi tidak ditemukan');
      }

      const category = await this.categoriesService.lockOwned(
        manager,
        userId,
        existing.categoryId,
      );
      category.balance = round2(
        category.balance - balanceEffect(existing.type, existing.amount),
      );
      await manager.save(category);
      await manager.remove(existing);
    });
  }

  private emitIfLowBalance(userId: string, category: Category) {
    if (category.balance < 0) {
      this.eventEmitter.emit(
        AppEvents.CATEGORY_LOW_BALANCE,
        new CategoryLowBalanceEvent(userId, category),
      );
    }
  }
}
