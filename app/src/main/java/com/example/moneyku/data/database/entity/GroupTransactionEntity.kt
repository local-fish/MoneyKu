package com.example.moneyku.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.UUID

@Entity(
    tableName = "group_transactions",
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
data class GroupTransactionEntity(
    @PrimaryKey
    @ColumnInfo(name = "group_transaction_id")
    val groupTransactionId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "category_id")
    val categoryId: String,
    @ColumnInfo(name = "total_amount")
    val totalAmount: BigDecimal = BigDecimal.ZERO,
    val tax: BigDecimal = BigDecimal.ZERO,
    val service: BigDecimal = BigDecimal.ZERO,
    @ColumnInfo(name = "group_transaction_date")
    val groupTransactionDate: String = System.currentTimeMillis().toString(),
    @ColumnInfo(name = "group_transaction_proof_url")
    val groupTransactionProofUrl: String? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: String = System.currentTimeMillis().toString(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: String = System.currentTimeMillis().toString()
)
