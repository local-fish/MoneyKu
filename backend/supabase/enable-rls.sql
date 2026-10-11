-- Jalankan di Supabase SQL Editor SETELAH tabel dibuat (start app sekali
-- dengan DB_SYNCHRONIZE=true).
--
-- Tabel di schema public otomatis terekspos lewat Supabase Data API
-- (anon / authenticated key). Mengaktifkan RLS tanpa policy membuat
-- tabel tidak bisa diakses lewat API tersebut. Backend NestJS tetap bisa
-- mengakses karena terhubung sebagai role `postgres` (pemilik tabel).

ALTER TABLE public.users               ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.main_accounts       ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.categories          ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.transactions        ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.group_transactions  ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.split_transactions  ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.notifications       ENABLE ROW LEVEL SECURITY;
