import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { EventEmitterModule } from '@nestjs/event-emitter';
import { TypeOrmModule } from '@nestjs/typeorm';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { AuthModule } from './auth/auth.module';
import { UsersModule } from './users/users.module';
import { AccountsModule } from './accounts/accounts.module';
import { CategoriesModule } from './categories/categories.module';
import { TransactionsModule } from './transactions/transactions.module';
import { GroupTransactionsModule } from './group-transactions/group-transactions.module';
import { NotificationsModule } from './notifications/notifications.module';

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true }),
    EventEmitterModule.forRoot(),
    TypeOrmModule.forRootAsync({
      inject: [ConfigService],
      useFactory: (config: ConfigService) => ({
        type: 'postgres',
        // Connection string Supabase (Project Settings → Database → Connect).
        url: config.getOrThrow<string>('DATABASE_URL'),
        // Supabase mewajibkan SSL.
        ssl:
          config.get<string>('DB_SSL', 'true') === 'true'
            ? { rejectUnauthorized: false }
            : false,
        // gen_random_uuid() bawaan Postgres, tidak butuh extension uuid-ossp.
        uuidExtension: 'pgcrypto',
        autoLoadEntities: true,
        // Hanya untuk development. Gunakan migration di production.
        synchronize: config.get<string>('DB_SYNCHRONIZE') === 'true',
        logging: config.get<string>('DB_LOGGING') === 'true',
      }),
    }),
    AuthModule,
    UsersModule,
    AccountsModule,
    CategoriesModule,
    TransactionsModule,
    GroupTransactionsModule,
    NotificationsModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
