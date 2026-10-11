import {
  ConflictException,
  Injectable,
  NotFoundException,
} from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import * as bcrypt from 'bcrypt';
import { Not, Repository } from 'typeorm';
import { User } from './entities/user.entity';
import { UpdateUserDto } from './dto/update-user.dto';

export const BCRYPT_ROUNDS = 10;

@Injectable()
export class UsersService {
  constructor(
    @InjectRepository(User) private readonly userRepo: Repository<User>,
  ) {}

  async findById(userId: string): Promise<User> {
    const user = await this.userRepo.findOneBy({ userId });
    if (!user) {
      throw new NotFoundException('User tidak ditemukan');
    }
    return user;
  }

  findByEmailWithPassword(email: string): Promise<User | null> {
    return this.userRepo
      .createQueryBuilder('user')
      .addSelect('user.password')
      .where('user.email = :email', { email })
      .getOne();
  }

  /** Lempar ConflictException jika email / username sudah dipakai user lain. */
  async assertUnique(
    { email, username }: { email?: string; username?: string },
    excludeUserId?: string,
  ): Promise<void> {
    const idFilter = excludeUserId ? { userId: Not(excludeUserId) } : {};
    if (email && (await this.userRepo.existsBy({ email, ...idFilter }))) {
      throw new ConflictException('Email sudah terdaftar');
    }
    if (username && (await this.userRepo.existsBy({ username, ...idFilter }))) {
      throw new ConflictException('Username sudah dipakai');
    }
  }

  async update(userId: string, dto: UpdateUserDto): Promise<User> {
    const user = await this.findById(userId);
    await this.assertUnique(dto, userId);

    const { password, ...rest } = dto;
    this.userRepo.merge(user, rest);
    if (password) {
      user.password = await bcrypt.hash(password, BCRYPT_ROUNDS);
    }
    await this.userRepo.save(user);
    return this.findById(userId);
  }
}
