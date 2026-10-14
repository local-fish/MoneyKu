package com.example.moneyku.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.moneyku.data.database.entity.GroupTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupTransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroupTransaction(groupTx: GroupTransactionEntity)

    @Update
    suspend fun updateGroupTransaction(groupTx: GroupTransactionEntity)

    @Delete
    suspend fun deleteGroupTransaction(groupTx: GroupTransactionEntity)

    @Query("SELECT * FROM group_transactions WHERE user_id = :userId ORDER BY group_transaction_date DESC")
    fun getGroupTransactionsByUserId(userId: String): Flow<List<GroupTransactionEntity>>
}
