# MoneyKu API

REST API untuk pengelolaan keuangan pribadi: mencatat riwayat transaksi, budget per kategori, dan split bill.

Stack: NestJS 11, TypeORM, PostgreSQL (Supabase), JWT, class-validator, `@nestjs/event-emitter` (Observer untuk notifikasi).

## Setup

1. Buat project di [supabase.com](https://supabase.com).
2. Buka **Connect** (tombol di atas dashboard) → tab **Connection String** → pilih **Session pooler**, lalu salin URL-nya.
3. Konfigurasi & jalankan:

   ```bash
   npm install
   cp .env.example .env      # isi DATABASE_URL (ganti [YOUR-PASSWORD]) & JWT_SECRET
   npm run start:dev
   ```

   Dengan `DB_SYNCHRONIZE=true`, tabel dibuat otomatis di Supabase saat start (development saja). Base URL: `http://localhost:3000/api`.
4. Jalankan [supabase/enable-rls.sql](supabase/enable-rls.sql) di **SQL Editor** Supabase supaya tabel tidak bisa dibaca lewat Supabase Data API (anon key).

> Pakai **Session pooler** (port 5432), bukan *Direct connection*. Direct connection Supabase hanya mendukung IPv6, jadi sering tidak bisa dipakai dari jaringan rumah.

## Struktur Tabel

| Tabel | Relasi | Catatan |
|---|---|---|
| `users` | – | `password` di-hash bcrypt, tidak pernah dikirim di response |
| `main_accounts` | 1:1 users | dibuat otomatis saat register. `balance` = dana yang belum dialokasikan |
| `categories` | n:1 main_accounts | `balance` = budget kategori |
| `transactions` | n:1 users, n:1 categories | `type`: `Income` / `Expense` |
| `group_transactions` | n:1 users, n:1 categories | tagihan split bill |
| `split_transactions` | n:1 group_transactions | `status`: `Pending` / `Completed` |
| `notifications` | n:1 users | dibuat oleh listener event (Observer) |

**Total budget tidak disimpan**: nilainya dihitung lewat query `SUM(categories.balance)` di `GET /account`.

## Alur Saldo

- **Deposit** → menambah `main_accounts.balance`.
- **Allocate / Release** → memindahkan dana antara main account dan kategori.
- **Transaksi Income / Expense** → menambah / mengurangi saldo kategori. Update & delete otomatis membatalkan efek lama.
- **Split bill** → kamu membayar seluruh tagihan dulu, jadi saldo kategori berkurang sebesar `total_amount`. Saat split seseorang ditandai `Completed` (sudah bayar ke kamu), nominalnya kembali ke kategori.
- Semua perubahan saldo berjalan di dalam DB transaction dengan row lock.

## Endpoint

Semua endpoint selain `/auth/*` butuh header `Authorization: Bearer <accessToken>`.

| Method | Path | Keterangan |
|---|---|---|
| POST | `/auth/register` | `{ username, email, password, phoneNumber?, avatarUrl? }` |
| POST | `/auth/login` | `{ email, password }` |
| GET / PATCH | `/users/me` | profil |
| GET | `/account` | saldo main account, `totalBudget`, `totalBalance` |
| POST | `/account/deposit` | `{ amount }` |
| GET / POST | `/categories` | `{ name }` |
| GET / PATCH / DELETE | `/categories/:id` | delete mengembalikan saldo ke main account |
| POST | `/categories/:id/allocate` | `{ amount }` main account → kategori |
| POST | `/categories/:id/release` | `{ amount }` kategori → main account |
| GET / POST | `/transactions` | filter: `type, categoryId, startDate, endDate, page, limit` |
| GET / PATCH / DELETE | `/transactions/:id` | |
| GET / POST | `/group-transactions` | lihat contoh di bawah |
| GET / DELETE | `/group-transactions/:id` | detail menyertakan `paidAmount` & `remainingAmount` |
| PATCH | `/group-transactions/:id/splits/:splitId` | `{ status: "Completed" \| "Pending" }` |
| GET | `/notifications?unreadOnly=true` | |
| PATCH | `/notifications/:id/read`, `/notifications/read-all` | |
| DELETE | `/notifications/:id` | |

### Contoh split bill

```json
POST /api/group-transactions
{
  "categoryId": "…",
  "tax": 11000,
  "service": 5000,
  "splits": [
    { "name": "Andi",  "amount": 50000 },
    { "name": "Budi",  "amount": 30000 },
    { "name": "Citra", "amount": 20000 }
  ]
}
```

`amount` tiap orang adalah harga pesanannya **sebelum** pajak & service. Pajak + service dibagi proporsional, jadi hasilnya Andi 58.000, Budi 34.800, Citra 23.200, dengan `total_amount` 116.000.

## Notifikasi (Observer)

Service hanya mem-publish event (`transaction.created`, `group-transaction.created`, `split.status-changed`, `category.low-balance`). `NotificationListener` men-subscribe event tersebut lalu menyimpan notifikasi. Untuk menambah channel lain (push, email), cukup tambah listener baru tanpa mengubah service.

## Koneksi dari Aplikasi Android (Kotlin)

Repo ini adalah backend untuk aplikasi mobile MoneyKu (Android, Kotlin).

| Menjalankan app di | Base URL |
|---|---|
| Android Emulator | `http://10.0.2.2:3000/api/` (`10.0.2.2` = `localhost` laptop) |
| HP fisik (satu Wi-Fi dengan laptop) | `http://<IP-laptop>:3000/api/`, cek IP dengan `ipconfig` |

- Android memblokir HTTP biasa secara default. Selama development, izinkan cleartext untuk host lokal lewat `network_security_config.xml` (atau `android:usesCleartextTraffic="true"`), dan pakai HTTPS di production.
- Simpan `accessToken` dari login (misalnya di DataStore), lalu kirim di setiap request sebagai header `Authorization: Bearer <token>`. Dengan Retrofit, cukup pasang satu OkHttp `Interceptor`.
- Response memakai camelCase (`categoryId`, `transactionDate`, …), jadi data class Kotlin bisa langsung dipetakan dengan Moshi / kotlinx.serialization.
- Field uang (`amount`, `balance`, …) dikirim sebagai number; tanggal sebagai string ISO-8601 (UTC).
- Error validasi mengembalikan `400` dengan `message` berupa array string; token tidak valid / kedaluwarsa mengembalikan `401`. Arahkan user ke halaman login saat menerima `401`.

## Scripts

```bash
npm run start:dev   # development (watch)
npm run build       # compile ke dist/
npm test            # unit test
npm run lint
```
