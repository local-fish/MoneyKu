package com.example.moneyku.data.model

data class TransactionItem(
    val id: String,
    val title: String,
    val category: String,
    val time: String,
    val amount: Long,
    val isExpense: Boolean,
    val statusText: String = "Selesai"
)

data class HomeScreenUiState(
    val userName: String = "Dimas",
    val dateText: String = "SENIN, 24 OKTOBER 2024",
    val totalBalance: Long = 14850000L,
    val monthlyGrowthPercent: Float = 4.2f,
    val totalIncome: Long = 18500000L,
    val totalExpense: Long = 3650000L,
    val budgetSpent: Long = 3650000L,
    val budgetLimit: Long = 7000000L,
    val recentTransactions: List<TransactionItem> = listOf(
        TransactionItem("1", "Kopi Kenangan", "Makanan & Minuman", "14:20 WIB", 38000L, true),
        TransactionItem("2", "Grab Transport", "Transportasi", "09:15 WIB", 24000L, true),
        TransactionItem("3", "Gaji Freelance UI", "Pemasukan", "Kemarin", 3500000L, false)
    )
)
