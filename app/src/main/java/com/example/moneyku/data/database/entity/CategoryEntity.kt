package com.example.moneyku.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.UUID

@Entity(
    tableName = "categories",
    foreignKeys = [
        ForeignKey(
            entity = MainAccountEntity::class,
            parentColumns = ["account_id"],
            childColumns = ["account_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("account_id")
    ]
)
data class CategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "category_id")
    val categoryId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "account_id")
    val accountId: String,
    val name: String,
    val balance: BigDecimal = BigDecimal.ZERO,
    @ColumnInfo(name = "created_at")
    val createdAt: String = System.currentTimeMillis().toString(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: String = System.currentTimeMillis().toString()
)
