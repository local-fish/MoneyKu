import {
  Body,
  Controller,
  Delete,
  Get,
  HttpCode,
  HttpStatus,
  Param,
  ParseUUIDPipe,
  Patch,
  Post,
  UseGuards,
} from '@nestjs/common';
import { JwtAuthGuard } from '../auth/jwt-auth.guard';
import { CurrentUser } from '../common/decorators/current-user.decorator';
import type { AuthUser } from '../common/decorators/current-user.decorator';
import { GroupTransactionsService } from './group-transactions.service';
import { CreateGroupTransactionDto } from './dto/create-group-transaction.dto';
import { UpdateSplitStatusDto } from './dto/update-split-status.dto';

@UseGuards(JwtAuthGuard)
@Controller('group-transactions')
export class GroupTransactionsController {
  constructor(
    private readonly groupTransactionsService: GroupTransactionsService,
  ) {}

  @Post()
  create(
    @CurrentUser() user: AuthUser,
    @Body() dto: CreateGroupTransactionDto,
  ) {
    return this.groupTransactionsService.create(user.userId, dto);
  }

  @Get()
  findAll(@CurrentUser() user: AuthUser) {
    return this.groupTransactionsService.findAll(user.userId);
  }

  @Get(':id')
  findOne(
    @CurrentUser() user: AuthUser,
    @Param('id', ParseUUIDPipe) id: string,
  ) {
    return this.groupTransactionsService.findOne(user.userId, id);
  }

  @Patch(':id/splits/:splitId')
  updateSplitStatus(
    @CurrentUser() user: AuthUser,
    @Param('id', ParseUUIDPipe) id: string,
    @Param('splitId', ParseUUIDPipe) splitId: string,
    @Body() dto: UpdateSplitStatusDto,
  ) {
    return this.groupTransactionsService.updateSplitStatus(
      user.userId,
      id,
      splitId,
      dto.status,
    );
  }

  @Delete(':id')
  @HttpCode(HttpStatus.NO_CONTENT)
  remove(
    @CurrentUser() user: AuthUser,
    @Param('id', ParseUUIDPipe) id: string,
  ) {
    return this.groupTransactionsService.remove(user.userId, id);
  }
}
