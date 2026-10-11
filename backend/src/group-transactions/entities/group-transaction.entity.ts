import {
  Column,
  CreateDateColumn,
  Entity,
  JoinColumn,
  ManyToOne,
  OneToMany,
  PrimaryGeneratedColumn,
  UpdateDateColumn,
} from 'typeorm';
import { moneyColumn } from '../../common/utils/money';
import { User } from '../../users/entities/user.entity';
import { Category } from '../../categories/entities/category.entity';
import { SplitTransaction } from './split-transaction.entity';

@Entity('group_transactions')
export class GroupTransaction {
  @PrimaryGeneratedColumn('uuid', { name: 'group_transaction_id' })
  groupTransactionId: string;

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

  @Column({ ...moneyColumn, name: 'total_amount' })
  totalAmount: number;

  @Column(moneyColumn)
  tax: number;

  @Column(moneyColumn)
  service: number;

  @Column({ name: 'group_transaction_date', type: 'timestamptz' })
  groupTransactionDate: Date;

  @Column({
    name: 'group_transaction_proof_url',
    type: 'varchar',
    length: 500,
    nullable: true,
  })
  groupTransactionProofUrl: string | null;

  @OneToMany(() => SplitTransaction, (split) => split.groupTransaction, {
    cascade: ['insert'],
  })
  splits: SplitTransaction[];

  @CreateDateColumn({ name: 'created_at', type: 'timestamptz' })
  createdAt: Date;

  @UpdateDateColumn({ name: 'updated_at', type: 'timestamptz' })
  updatedAt: Date;
}
