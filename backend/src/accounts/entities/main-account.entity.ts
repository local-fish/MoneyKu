import {
  Column,
  CreateDateColumn,
  Entity,
  JoinColumn,
  OneToMany,
  OneToOne,
  PrimaryGeneratedColumn,
  UpdateDateColumn,
} from 'typeorm';
import { moneyColumn } from '../../common/utils/money';
import { User } from '../../users/entities/user.entity';
import { Category } from '../../categories/entities/category.entity';

/**
 * Akun utama (1:1 dengan user). `balance` adalah dana yang belum
 * dialokasikan ke kategori mana pun ("dump"). Total budget dihitung
 * lewat query SUM(categories.balance), tidak disimpan.
 */
@Entity('main_accounts')
export class MainAccount {
  @PrimaryGeneratedColumn('uuid', { name: 'account_id' })
  accountId: string;

  @Column({ name: 'user_id', type: 'uuid', unique: true })
  userId: string;

  @OneToOne(() => User, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'user_id' })
  user: User;

  @Column(moneyColumn)
  balance: number;

  @OneToMany(() => Category, (category) => category.account)
  categories: Category[];

  @CreateDateColumn({ name: 'created_at', type: 'timestamptz' })
  createdAt: Date;

  @UpdateDateColumn({ name: 'updated_at', type: 'timestamptz' })
  updatedAt: Date;
}
