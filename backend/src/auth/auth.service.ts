import { Injectable, UnauthorizedException } from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';
import * as bcrypt from 'bcrypt';
import { DataSource } from 'typeorm';
import { User } from '../users/entities/user.entity';
import { BCRYPT_ROUNDS, UsersService } from '../users/users.service';
import { MainAccount } from '../accounts/entities/main-account.entity';
import { RegisterDto } from './dto/register.dto';
import { LoginDto } from './dto/login.dto';
import { JwtPayload } from './jwt.strategy';

@Injectable()
export class AuthService {
  constructor(
    private readonly dataSource: DataSource,
    private readonly usersService: UsersService,
    private readonly jwtService: JwtService,
  ) {}

  /** Register user baru sekaligus membuat MainAccount-nya (1:1). */
  async register(dto: RegisterDto) {
    await this.usersService.assertUnique(dto);
    const password = await bcrypt.hash(dto.password, BCRYPT_ROUNDS);

    const userId = await this.dataSource.transaction(async (manager) => {
      const user = await manager.save(
        manager.create(User, { ...dto, password }),
      );
      await manager.save(
        manager.create(MainAccount, { userId: user.userId, balance: 0 }),
      );
      return user.userId;
    });

    const user = await this.usersService.findById(userId);
    return this.buildAuthResponse(user);
  }

  async login(dto: LoginDto) {
    const user = await this.usersService.findByEmailWithPassword(dto.email);
    if (!user || !(await bcrypt.compare(dto.password, user.password))) {
      throw new UnauthorizedException('Email atau password salah');
    }
    return this.buildAuthResponse(
      await this.usersService.findById(user.userId),
    );
  }

  private async buildAuthResponse(user: User) {
    const payload: JwtPayload = { sub: user.userId, email: user.email };
    return {
      accessToken: await this.jwtService.signAsync(payload),
      user,
    };
  }
}
