import {
  IsEmail,
  IsOptional,
  IsString,
  IsUrl,
  Length,
  Matches,
  MinLength,
} from 'class-validator';

export class RegisterDto {
  @IsString()
  @Length(3, 50)
  @Matches(/^[a-zA-Z0-9_.]+$/, {
    message: 'username hanya boleh huruf, angka, titik, dan underscore',
  })
  username: string;

  @IsEmail()
  email: string;

  @IsString()
  @MinLength(8)
  password: string;

  @IsOptional()
  @Matches(/^\+?[0-9]{8,15}$/, { message: 'phoneNumber tidak valid' })
  phoneNumber?: string;

  @IsOptional()
  @IsUrl()
  avatarUrl?: string;
}
