import { Body, Controller, Get, Post, UseGuards } from '@nestjs/common';
import { JwtAuthGuard } from '../auth/jwt-auth.guard';
import { CurrentUser } from '../common/decorators/current-user.decorator';
import type { AuthUser } from '../common/decorators/current-user.decorator';
import { AmountDto } from '../common/dto/amount.dto';
import { AccountsService } from './accounts.service';

@UseGuards(JwtAuthGuard)
@Controller('account')
export class AccountsController {
  constructor(private readonly accountsService: AccountsService) {}

  @Get()
  getSummary(@CurrentUser() user: AuthUser) {
    return this.accountsService.getSummary(user.userId);
  }

  @Post('deposit')
  deposit(@CurrentUser() user: AuthUser, @Body() dto: AmountDto) {
    return this.accountsService.deposit(user.userId, dto.amount);
  }
}
