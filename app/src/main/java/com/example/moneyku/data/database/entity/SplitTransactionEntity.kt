package com.example.moneyku.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.UUID

@Entity(
    tableName = "split_transactions",
    foreignKeys = [
        ForeignKey(
            entity = GroupTransactionEntity::class,
            parentColumns = ["group_transaction_id"],
            childColumns = ["group_transaction_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("group_transaction_id")
    ]
)
data class SplitTransactionEntity(
    @PrimaryKey
    @ColumnInfo(name = "split_transaction_id")
    val splitTransactionId: String = UUID.randomUUID().toString(),
    val name: String,
    val amount: BigDecimal,
    @ColumnInfo(name = "group_transaction_id")
    val groupTransactionId: String,
    val status: SplitTransactionStatus = SplitTransactionStatus.Pending,
    @ColumnInfo(name = "created_at")
    val createdAt: String = System.currentTimeMillis().toString(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: String = System.currentTimeMillis().toString()
)
