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
import { User } from '../../users/entities/user.entity';
import { Category } from '../../categories/entities/category.entity';

export enum TransactionType {
  INCOME = 'Income',
  EXPENSE = 'Expense',
}

@Entity('transactions')
export class Transaction {
  @PrimaryGeneratedColumn('uuid', { name: 'transaction_id' })
  transactionId: string;

  @Column({ name: 'user_id', type: 'uuid' })
  userId: string;

  @ManyToOne(() => User, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'user_id' })
  user: User;

  @Column({ name: 'category_id', type: 'uuid' })
  categoryId: string;

  @ManyToOne(() => Category, { onDelete: 'RESTRICT' })
  @JoinColumn({ name: 'category_id' })
  category: Category;

  @Column({ type: 'enum', enum: TransactionType })
  type: TransactionType;

  @Column({ ...moneyColumn, default: undefined })
  amount: number;

  @Column({ type: 'varchar', length: 150 })
  title: string;

  @Column({ type: 'text', nullable: true })
  note: string | null;

  @Column({ name: 'transaction_date', type: 'timestamptz' })
  transactionDate: Date;

  @Column({
    name: 'transaction_proof_url',
    type: 'varchar',
    length: 500,
    nullable: true,
  })
  transactionProofUrl: string | null;

  @CreateDateColumn({ name: 'created_at', type: 'timestamptz' })
  createdAt: Date;

  @UpdateDateColumn({ name: 'updated_at', type: 'timestamptz' })
  updatedAt: Date;
}
