import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Notification, NotificationType } from './entities/notification.entity';

@Injectable()
export class NotificationsService {
  constructor(
    @InjectRepository(Notification)
    private readonly notificationRepo: Repository<Notification>,
  ) {}

  create(
    userId: string,
    type: NotificationType,
    title: string,
    message: string,
  ): Promise<Notification> {
    return this.notificationRepo.save(
      this.notificationRepo.create({ userId, type, title, message }),
    );
  }

  findAll(userId: string, unreadOnly = false): Promise<Notification[]> {
    return this.notificationRepo.find({
      where: { userId, ...(unreadOnly && { isRead: false }) },
      order: { createdAt: 'DESC' },
    });
  }

  async markAsRead(
    userId: string,
    notificationId: string,
  ): Promise<Notification> {
    const notification = await this.notificationRepo.findOneBy({
      notificationId,
      userId,
    });
    if (!notification) {
      throw new NotFoundException('Notifikasi tidak ditemukan');
    }
    notification.isRead = true;
    return this.notificationRepo.save(notification);
  }

  async markAllAsRead(userId: string): Promise<{ updated: number }> {
    const result = await this.notificationRepo.update(
      { userId, isRead: false },
      { isRead: true },
    );
    return { updated: result.affected ?? 0 };
  }

  async remove(userId: string, notificationId: string): Promise<void> {
    const result = await this.notificationRepo.delete({
      notificationId,
      userId,
    });
    if (!result.affected) {
      throw new NotFoundException('Notifikasi tidak ditemukan');
    }
  }
}
