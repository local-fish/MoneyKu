import { Injectable, Logger } from '@nestjs/common';
import { OnEvent } from '@nestjs/event-emitter';
import { formatRupiah } from '../common/utils/money';
import { TransactionType } from '../transactions/entities/transaction.entity';
import { SplitStatus } from '../group-transactions/entities/split-transaction.entity';
import { NotificationType } from './entities/notification.entity';
import { NotificationsService } from './notifications.service';
import {
  AppEvents,
  CategoryLowBalanceEvent,
  GroupTransactionCreatedEvent,
  SplitStatusChangedEvent,
  TransactionCreatedEvent,
} from './notification.events';

/** Observer: mendengarkan event domain lalu membuat notifikasi. */
@Injectable()
export class NotificationListener {
  private readonly logger = new Logger(NotificationListener.name);

  constructor(private readonly notificationsService: NotificationsService) {}

  @OnEvent(AppEvents.TRANSACTION_CREATED)
  async onTransactionCreated({ userId, transaction }: TransactionCreatedEvent) {
    const label =
      transaction.type === TransactionType.INCOME ? 'Pemasukan' : 'Pengeluaran';
    await this.notify(
      userId,
      NotificationType.TRANSACTION,
      `${label} tercatat`,
      `${label} "${transaction.title}" sebesar ${formatRupiah(transaction.amount)} berhasil dicatat.`,
    );
  }

  @OnEvent(AppEvents.GROUP_TRANSACTION_CREATED)
  async onGroupTransactionCreated({
    userId,
    groupTransaction,
  }: GroupTransactionCreatedEvent) {
    await this.notify(
      userId,
      NotificationType.GROUP_TRANSACTION,
      'Split bill dibuat',
      `Tagihan ${formatRupiah(groupTransaction.totalAmount)} dibagi ke ${groupTransaction.splits.length} orang.`,
    );
  }

  @OnEvent(AppEvents.SPLIT_STATUS_CHANGED)
  async onSplitStatusChanged({ userId, split }: SplitStatusChangedEvent) {
    const message =
      split.status === SplitStatus.COMPLETED
        ? `${split.name} sudah membayar ${formatRupiah(split.amount)}.`
        : `Pembayaran ${split.name} (${formatRupiah(split.amount)}) dikembalikan ke status Pending.`;
    await this.notify(
      userId,
      NotificationType.SPLIT_PAYMENT,
      'Status split bill diperbarui',
      message,
    );
  }

  @OnEvent(AppEvents.CATEGORY_LOW_BALANCE)
  async onCategoryLowBalance({ userId, category }: CategoryLowBalanceEvent) {
    await this.notify(
      userId,
      NotificationType.LOW_BALANCE,
      'Budget kategori minus',
      `Saldo kategori "${category.name}" sekarang ${formatRupiah(category.balance)}.`,
    );
  }

  private async notify(
    userId: string,
    type: NotificationType,
    title: string,
    message: string,
  ) {
    // Error di listener tidak boleh menggagalkan request utama.
    try {
      await this.notificationsService.create(userId, type, title, message);
    } catch (error) {
      this.logger.error(`Gagal membuat notifikasi: ${String(error)}`);
    }
  }
}
