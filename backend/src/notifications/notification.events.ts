import { Category } from '../categories/entities/category.entity';
import { Transaction } from '../transactions/entities/transaction.entity';
import { GroupTransaction } from '../group-transactions/entities/group-transaction.entity';
import { SplitTransaction } from '../group-transactions/entities/split-transaction.entity';

/**
 * Nama event domain. Service lain hanya mem-publish event (subject),
 * NotificationListener yang men-subscribe (observer).
 */
export const AppEvents = {
  TRANSACTION_CREATED: 'transaction.created',
  GROUP_TRANSACTION_CREATED: 'group-transaction.created',
  SPLIT_STATUS_CHANGED: 'split.status-changed',
  CATEGORY_LOW_BALANCE: 'category.low-balance',
} as const;

export class TransactionCreatedEvent {
  constructor(
    public readonly userId: string,
    public readonly transaction: Transaction,
  ) {}
}

export class GroupTransactionCreatedEvent {
  constructor(
    public readonly userId: string,
    public readonly groupTransaction: GroupTransaction,
  ) {}
}

export class SplitStatusChangedEvent {
  constructor(
    public readonly userId: string,
    public readonly split: SplitTransaction,
  ) {}
}

export class CategoryLowBalanceEvent {
  constructor(
    public readonly userId: string,
    public readonly category: Category,
  ) {}
}
