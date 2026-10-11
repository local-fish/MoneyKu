import { Injectable, NotFoundException } from '@nestjs/common';
import { EventEmitter2 } from '@nestjs/event-emitter';
import { InjectDataSource, InjectRepository } from '@nestjs/typeorm';
import { DataSource, Repository } from 'typeorm';
import { round2 } from '../common/utils/money';
import { CategoriesService } from '../categories/categories.service';
import {
  AppEvents,
  CategoryLowBalanceEvent,
  GroupTransactionCreatedEvent,
  SplitStatusChangedEvent,
} from '../notifications/notification.events';
import { GroupTransaction } from './entities/group-transaction.entity';
import {
  SplitStatus,
  SplitTransaction,
} from './entities/split-transaction.entity';
import { CreateGroupTransactionDto } from './dto/create-group-transaction.dto';
import { calculateSplits } from './split-calculator';

@Injectable()
export class GroupTransactionsService {
  constructor(
    @InjectRepository(GroupTransaction)
    private readonly groupRepo: Repository<GroupTransaction>,
    @InjectDataSource() private readonly dataSource: DataSource,
    private readonly categoriesService: CategoriesService,
    private readonly eventEmitter: EventEmitter2,
  ) {}

  /**
   * User membayar seluruh tagihan dulu â†’ saldo kategori berkurang sebesar
   * total. Saat teman melunasi (split Completed), dananya kembali ke kategori.
   */
  async create(userId: string, dto: CreateGroupTransactionDto) {
    const { totalAmount, splits } = calculateSplits(
      dto.splits,
      dto.tax,
      dto.service,
    );

    const { group, category } = await this.dataSource.transaction(
      async (manager) => {
        const category = await this.categoriesService.lockOwned(
          manager,
          userId,
          dto.categoryId,
        );
        category.balance = round2(category.balance - totalAmount);
        await manager.save(category);

        const group = await manager.save(
          manager.create(GroupTransaction, {
            userId,
            categoryId: dto.categoryId,
            totalAmount,
            tax: dto.tax,
            service: dto.service,
            groupTransactionDate: dto.groupTransactionDate
              ? new Date(dto.groupTransactionDate)
              : new Date(),
            groupTransactionProofUrl: dto.groupTransactionProofUrl ?? null,
            splits: splits.map((split) =>
              manager.create(SplitTransaction, {
                ...split,
                status: SplitStatus.PENDING,
              }),
            ),
          }),
        );
        return { group, category };
      },
    );

    this.eventEmitter.emit(
      AppEvents.GROUP_TRANSACTION_CREATED,
      new GroupTransactionCreatedEvent(userId, group),
    );
    if (category.balance < 0) {
      this.eventEmitter.emit(
        AppEvents.CATEGORY_LOW_BALANCE,
        new CategoryLowBalanceEvent(userId, category),
      );
    }
    return this.findOne(userId, group.groupTransactionId);
  }

  findAll(userId: string): Promise<GroupTransaction[]> {
    return this.groupRepo.find({
      where: { userId },
      relations: { splits: true, category: true },
      order: { groupTransactionDate: 'DESC', splits: { createdAt: 'ASC' } },
    });
  }

  async findOne(userId: string, groupTransactionId: string) {
    const group = await this.groupRepo.findOne({
      where: { groupTransactionId, userId },
      relations: { splits: true, category: true },
      order: { splits: { createdAt: 'ASC' } },
    });
    if (!group) {
      throw new NotFoundException('Group transaction tidak ditemukan');
    }

    const paid = round2(
      group.splits
        .filter((split) => split.status === SplitStatus.COMPLETED)
        .reduce((sum, split) => sum + split.amount, 0),
    );
    return {
      ...group,
      paidAmount: paid,
      remainingAmount: round2(group.totalAmount - paid),
    };
  }

  async updateSplitStatus(
    userId: string,
    groupTransactionId: string,
    splitTransactionId: string,
    status: SplitStatus,
  ): Promise<SplitTransaction> {
    const { split, changed } = await this.dataSource.transaction(
      async (manager) => {
        const group = await manager.findOneBy(GroupTransaction, {
          groupTransactionId,
          userId,
        });
        if (!group) {
          throw new NotFoundException('Group transaction tidak ditemukan');
        }

        const split = await manager.findOne(SplitTransaction, {
          where: { splitTransactionId, groupTransactionId },
          lock: { mode: 'pessimistic_write' },
        });
        if (!split) {
          throw new NotFoundException('Split transaction tidak ditemukan');
        }
        if (split.status === status) {
          return { split, changed: false };
        }

        const category = await this.categoriesService.lockOwned(
          manager,
          userId,
          group.categoryId,
        );
        const delta =
          status === SplitStatus.COMPLETED ? split.amount : -split.amount;
        category.balance = round2(category.balance + delta);
        split.status = status;
        await manager.save(category);
        return { split: await manager.save(split), changed: true };
      },
    );

    if (changed) {
      this.eventEmitter.emit(
        AppEvents.SPLIT_STATUS_CHANGED,
        new SplitStatusChangedEvent(userId, split),
      );
    }
    return split;
  }

  /** Hapus group transaction; dana yang belum kembali dikembalikan ke kategori. */
  async remove(userId: string, groupTransactionId: string): Promise<void> {
    await this.dataSource.transaction(async (manager) => {
      const group = await manager.findOne(GroupTransaction, {
        where: { groupTransactionId, userId },
        relations: { splits: true },
      });
      if (!group) {
        throw new NotFoundException('Group transaction tidak ditemukan');
      }

      const reimbursed = group.splits
        .filter((split) => split.status === SplitStatus.COMPLETED)
        .reduce((sum, split) => sum + split.amount, 0);

      const category = await this.categoriesService.lockOwned(
        manager,
        userId,
        group.categoryId,
      );
      category.balance = round2(
        category.balance + group.totalAmount - reimbursed,
      );
      await manager.save(category);
      await manager.delete(GroupTransaction, { groupTransactionId });
    });
  }
}
