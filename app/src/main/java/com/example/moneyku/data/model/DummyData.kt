package com.example.moneyku.data.model

import java.math.BigDecimal

data class WalletItem(
    val id: Long,
    val name: String,
    val iconName: String,
    val balance: BigDecimal,
    val accountNumber: String? = null
)

data class DummyTransactionItem(
    val id: Long,
    val title: String,
    val categoryName: String,
    val categoryIcon: String,
    val dateText: String,
    val amount: BigDecimal,
    val isExpense: Boolean,
    val walletName: String
)

data class SplitParticipantItem(
    val id: Long,
    val name: String,
    val amountOwed: BigDecimal,
    val isPaid: Boolean
)

data class SplitBillItem(
    val id: Long,
    val title: String,
    val totalAmount: BigDecimal,
    val dateText: String,
    val statusText: String,
    val isSettled: Boolean,
    val participants: List<SplitParticipantItem>
)

data class BudgetItem(
    val id: Long,
    val categoryName: String,
    val iconName: String,
    val spentAmount: BigDecimal,
    val limitAmount: BigDecimal,
    val statusText: String
)

object DummyData {

    val wallets = listOf(
        WalletItem(1, "BCA", "ic_bank", BigDecimal("12500000"), "**** 8821"),
        WalletItem(2, "Gopay", "ic_wallet", BigDecimal("350000"), "0812****99"),
        WalletItem(3, "Tunai", "ic_cash", BigDecimal("150000"))
    )

    val transactions = listOf(
        DummyTransactionItem(
            id = 1,
            title = "Ayam Geprek Pak Gembus",
            categoryName = "Makanan",
            categoryIcon = "food",
            dateText = "Hari ini, 12:30",
            amount = BigDecimal("28000"),
            isExpense = true,
            walletName = "Gopay"
        ),
        DummyTransactionItem(
            id = 2,
            title = "Transfer Kiriman Orang Tua",
            categoryName = "Pemasukan",
            categoryIcon = "income",
            dateText = "Kemarin, 09:15",
            amount = BigDecimal("2500000"),
            isExpense = false,
            walletName = "BCA"
        ),
        DummyTransactionItem(
            id = 3,
            title = "Bensin Pertalite Motor",
            categoryName = "Transport",
            categoryIcon = "transport",
            dateText = "12 Feb 2026, 17:40",
            amount = BigDecimal("35000"),
            isExpense = true,
            walletName = "Tunai"
        ),
        DummyTransactionItem(
            id = 4,
            title = "Kopi Susu & Nongkrong",
            categoryName = "Hiburan",
            categoryIcon = "coffee",
            dateText = "11 Feb 2026, 20:10",
            amount = BigDecimal("45000"),
            isExpense = true,
            walletName = "Gopay"
        ),
        DummyTransactionItem(
            id = 5,
            title = "Pelunasan Split Bill - Budi",
            categoryName = "Adjustment",
            categoryIcon = "split",
            dateText = "10 Feb 2026, 14:00",
            amount = BigDecimal("65000"),
            isExpense = false,
            walletName = "BCA"
        ),
        DummyTransactionItem(
            id = 6,
            title = "Beli Buku Kuliah / Modul",
            categoryName = "Pendidikan",
            categoryIcon = "education",
            dateText = "08 Feb 2026, 11:20",
            amount = BigDecimal("120000"),
            isExpense = true,
            walletName = "BCA"
        )
    )

    val splitBills = listOf(
        SplitBillItem(
            id = 1,
            title = "Nasi Goreng Kampus (4 Orang)",
            totalAmount = BigDecimal("100000"),
            dateText = "Hari ini, 13:00",
            statusText = "Pending",
            isSettled = false,
            participants = listOf(
                SplitParticipantItem(1, "Budi", BigDecimal("30000"), isPaid = true),
                SplitParticipantItem(2, "Siti", BigDecimal("30000"), isPaid = false),
                SplitParticipantItem(3, "Andi", BigDecimal("40000"), isPaid = false)
            )
        ),
        SplitBillItem(
            id = 2,
            title = "Sewa Lapangan Futsal",
            totalAmount = BigDecimal("240000"),
            dateText = "08 Feb 2026",
            statusText = "Lunas",
            isSettled = true,
            participants = listOf(
                SplitParticipantItem(4, "Rian", BigDecimal("60000"), isPaid = true),
                SplitParticipantItem(5, "Doni", BigDecimal("60000"), isPaid = true),
                SplitParticipantItem(6, "Eko", BigDecimal("60000"), isPaid = true)
            )
        )
    )

    val budgets = listOf(
        BudgetItem(
            id = 1,
            categoryName = "Makanan & Minuman",
            iconName = "food",
            spentAmount = BigDecimal("850000"),
            limitAmount = BigDecimal("1200000"),
            statusText = "Aman"
        ),
        BudgetItem(
            id = 2,
            categoryName = "Transportasi",
            iconName = "transport",
            spentAmount = BigDecimal("280000"),
            limitAmount = BigDecimal("300000"),
            statusText = "Hampir Habis"
        ),
        BudgetItem(
            id = 3,
            categoryName = "Hiburan & Nongkrong",
            iconName = "entertainment",
            spentAmount = BigDecimal("550000"),
            limitAmount = BigDecimal("500000"),
            statusText = "Over Budget"
        ),
        BudgetItem(
            id = 4,
            categoryName = "Belanja & Kebutuhan",
            iconName = "shopping",
            spentAmount = BigDecimal("320000"),
            limitAmount = BigDecimal("600000"),
            statusText = "Aman"
        )
    )
}
