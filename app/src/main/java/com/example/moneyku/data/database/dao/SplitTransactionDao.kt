package com.example.moneyku.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.moneyku.data.database.entity.SplitTransactionEntity
import com.example.moneyku.data.database.entity.SplitTransactionStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface SplitTransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSplitTransaction(splitTx: SplitTransactionEntity)

    @Update
    suspend fun updateSplitTransaction(splitTx: SplitTransactionEntity)

    @Delete
    suspend fun deleteSplitTransaction(splitTx: SplitTransactionEntity)

    @Query("SELECT * FROM split_transactions WHERE group_transaction_id = :groupTransactionId")
    fun getSplitTransactionsByGroupTxId(groupTransactionId: String): Flow<List<SplitTransactionEntity>>

    @Query("UPDATE split_transactions SET status = :status WHERE split_transaction_id = :splitTransactionId")
    suspend fun updateSplitStatus(splitTransactionId: String, status: SplitTransactionStatus)
}
