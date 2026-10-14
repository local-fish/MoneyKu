package com.example.moneyku.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.moneyku.data.database.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Query("SELECT * FROM categories WHERE account_id = :accountId")
    fun getCategoriesByAccountId(accountId: String): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE category_id = :categoryId LIMIT 1")
    suspend fun getCategoryById(categoryId: String): CategoryEntity?
}
