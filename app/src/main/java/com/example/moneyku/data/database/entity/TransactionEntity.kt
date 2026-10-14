package com.example.moneyku.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.UUID

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["category_id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("user_id"),
        Index("category_id")
    ]
)
data class TransactionEntity(
    @PrimaryKey
    @ColumnInfo(name = "transaction_id")
    val transactionId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "category_id")
    val categoryId: String,
    val type: TransactionType,
    val amount: BigDecimal,
    val title: String,
    val note: String? = null,
    @ColumnInfo(name = "transaction_date")
    val transactionDate: String = System.currentTimeMillis().toString(),
    @ColumnInfo(name = "transaction_proof_url")
    val transactionProofUrl: String? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: String = System.currentTimeMillis().toString(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: String = System.currentTimeMillis().toString()
)
