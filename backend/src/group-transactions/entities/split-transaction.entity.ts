import {
  Column,
  CreateDateColumn,
  Entity,
  JoinColumn,
  ManyToOne,
  PrimaryGeneratedColumn,
  UpdateDateColumn,
} from 'typeorm';
import { moneyColumn } from '../../common/utils/money';
import { GroupTransaction } from './group-transaction.entity';

export enum SplitStatus {
  PENDING = 'Pending',
  COMPLETED = 'Completed',
}

@Entity('split_transactions')
export class SplitTransaction {
  @PrimaryGeneratedColumn('uuid', { name: 'split_transaction_id' })
  splitTransactionId: string;

  @Column({ type: 'varchar', length: 100 })
  name: string;

  @Column({ ...moneyColumn, default: undefined })
  amount: number;

  @Column({ name: 'group_transaction_id', type: 'uuid' })
  groupTransactionId: string;

  @ManyToOne(() => GroupTransaction, (group) => group.splits, {
    onDelete: 'CASCADE',
  })
  @JoinColumn({ name: 'group_transaction_id' })
  groupTransaction: GroupTransaction;

  @Column({ type: 'enum', enum: SplitStatus, default: SplitStatus.PENDING })
  status: SplitStatus;

  @CreateDateColumn({ name: 'created_at', type: 'timestamptz' })
  createdAt: Date;

  @UpdateDateColumn({ name: 'updated_at', type: 'timestamptz' })
  updatedAt: Date;
}
