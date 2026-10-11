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
import { MainAccount } from '../../accounts/entities/main-account.entity';

@Entity('categories')
export class Category {
  @PrimaryGeneratedColumn('uuid', { name: 'category_id' })
  categoryId: string;

  @Column({ name: 'account_id', type: 'uuid' })
  accountId: string;

  @ManyToOne(() => MainAccount, (account) => account.categories, {
    onDelete: 'CASCADE',
  })
  @JoinColumn({ name: 'account_id' })
  account: MainAccount;

  @Column({ type: 'varchar', length: 100 })
  name: string;

  @Column(moneyColumn)
  balance: number;

  @CreateDateColumn({ name: 'created_at', type: 'timestamptz' })
  createdAt: Date;

  @UpdateDateColumn({ name: 'updated_at', type: 'timestamptz' })
  updatedAt: Date;
}
