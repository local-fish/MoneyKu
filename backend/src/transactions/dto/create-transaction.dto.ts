import {
  IsDateString,
  IsEnum,
  IsNotEmpty,
  IsNumber,
  IsOptional,
  IsPositive,
  IsString,
  IsUrl,
  IsUUID,
  MaxLength,
} from 'class-validator';
import { TransactionType } from '../entities/transaction.entity';

export class CreateTransactionDto {
  @IsUUID()
  categoryId: string;

  @IsEnum(TransactionType)
  type: TransactionType;

  @IsNumber({ maxDecimalPlaces: 2 })
  @IsPositive()
  amount: number;

  @IsString()
  @IsNotEmpty()
  @MaxLength(150)
  title: string;

  @IsOptional()
  @IsString()
  note?: string;

  /** Default: waktu sekarang. */
  @IsOptional()
  @IsDateString()
  transactionDate?: string;

  @IsOptional()
  @IsUrl()
  transactionProofUrl?: string;
}
