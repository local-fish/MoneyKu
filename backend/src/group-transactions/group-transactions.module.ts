import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { CategoriesModule } from '../categories/categories.module';
import { GroupTransaction } from './entities/group-transaction.entity';
import { SplitTransaction } from './entities/split-transaction.entity';
import { GroupTransactionsService } from './group-transactions.service';
import { GroupTransactionsController } from './group-transactions.controller';

@Module({
  imports: [
    TypeOrmModule.forFeature([GroupTransaction, SplitTransaction]),
    CategoriesModule,
  ],
  controllers: [GroupTransactionsController],
  providers: [GroupTransactionsService],
})
export class GroupTransactionsModule {}
