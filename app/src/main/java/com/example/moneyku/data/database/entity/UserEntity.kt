package com.example.moneyku.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "users",
    indices = [
        Index(value = ["username"], unique = true),
        Index(value = ["email"], unique = true)
    ]
)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: String = UUID.randomUUID().toString(),
    val username: String,
    val email: String,
    val password: String,
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String? = null,
    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: String = System.currentTimeMillis().toString(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: String = System.currentTimeMillis().toString()
)
