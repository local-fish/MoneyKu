package com.example.moneyku.data.remote

/**
 * Template Konfigurasi Supabase Client untuk MoneyKu.
 *
 * Silakan isi SUPABASE_URL dan SUPABASE_ANON_KEY sesuai dengan project Supabase Anda.
 * Setelah SDK Supabase (`io.github.jan-tennert.supabase`) ditambahkan ke `build.gradle.kts`,
 * inisialisasi client dapat diaktifkan:
 *
 * val supabase = createSupabaseClient(
 *     supabaseUrl = SupabaseClientTemplate.SUPABASE_URL,
 *     supabaseKey = SupabaseClientTemplate.SUPABASE_ANON_KEY
 * ) {
 *     install(Auth)
 *     install(Postgrest)
 * }
 */
object SupabaseClientTemplate {
    const val SUPABASE_URL = "YOUR_SUPABASE_URL_HERE"
    const val SUPABASE_ANON_KEY = "YOUR_SUPABASE_KEY_HERE"

    // Multi-table schema reference
    const val TABLE_USERS = "users"
    const val TABLE_WALLETS = "wallets"
    const val TABLE_CATEGORIES = "categories"
    const val TABLE_TRANSACTIONS = "transactions"
    const val TABLE_SPLIT_BILLS = "split_bills"
    const val TABLE_SPLIT_PARTICIPANTS = "split_participants"
    const val TABLE_BUDGETS = "budgets"
}
