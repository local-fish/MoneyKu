package com.example.moneyku.data.database.entity

enum class TransactionType {
    INCOME,
    EXPENSE,
    ADJUSTMENT
}

enum class SplitTransactionStatus {
    Pending,
    Paid
}

enum class NotificationType {
    INFO,
    SPLIT_BILL,
    SYSTEM
}
