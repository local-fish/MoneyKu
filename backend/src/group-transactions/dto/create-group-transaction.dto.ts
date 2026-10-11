import { Type } from 'class-transformer';
import {
  ArrayMinSize,
  IsArray,
  IsDateString,
  IsNotEmpty,
  IsNumber,
  IsOptional,
  IsPositive,
  IsString,
  IsUrl,
  IsUUID,
  MaxLength,
  Min,
  ValidateNested,
} from 'class-validator';

export class CreateSplitDto {
  @IsString()
  @IsNotEmpty()
  @MaxLength(100)
  name: string;

  /** Harga pesanan orang ini SEBELUM pajak & service. */
  @IsNumber({ maxDecimalPlaces: 2 })
  @IsPositive()
  amount: number;
}

export class CreateGroupTransactionDto {
  @IsUUID()
  categoryId: string;

  /** Nominal pajak (bukan persen). */
  @IsOptional()
  @IsNumber({ maxDecimalPlaces: 2 })
  @Min(0)
  tax: number = 0;

  /** Nominal service charge (bukan persen). */
  @IsOptional()
  @IsNumber({ maxDecimalPlaces: 2 })
  @Min(0)
  service: number = 0;

  /** Default: waktu sekarang. */
  @IsOptional()
  @IsDateString()
  groupTransactionDate?: string;

  @IsOptional()
  @IsUrl()
  groupTransactionProofUrl?: string;

  @IsArray()
  @ArrayMinSize(1)
  @ValidateNested({ each: true })
  @Type(() => CreateSplitDto)
  splits: CreateSplitDto[];
}
