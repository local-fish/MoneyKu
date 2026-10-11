import { IsEnum } from 'class-validator';
import { SplitStatus } from '../entities/split-transaction.entity';

export class UpdateSplitStatusDto {
  @IsEnum(SplitStatus)
  status: SplitStatus;
}
