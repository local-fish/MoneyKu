# MoneyKu

Aplikasi mobile untuk mengatur keuangan: mencatat riwayat transaksi, budget per kategori, dan split bill.

## Struktur Repo

| Folder | Isi | Stack |
|---|---|---|
| [`backend/`](backend/) | REST API | NestJS, TypeORM, PostgreSQL (Supabase), JWT |
| `android/` | Aplikasi mobile (menyusul) | Android Studio, Kotlin |

## Menjalankan Backend

```bash
cd backend
npm install
cp .env.example .env   # isi DATABASE_URL Supabase & JWT_SECRET
npm run start:dev
```

Detail endpoint, alur saldo, dan cara menghubungkan aplikasi Android ada di [backend/README.md](backend/README.md).

## Menambahkan Project Android

Buat project baru di Android Studio dengan lokasi `<repo>/android`. Android Studio otomatis membuat `.gitignore` sendiri di folder tersebut.

## Alur Git

- `main` hanya berisi kode yang sudah stabil.
- Development dilakukan di branch `development` (atau feature branch dari `development`), lalu di-merge ke `main` lewat pull request.
- Rebase ke branch tujuan sebelum push: `git pull --rebase origin development`.
