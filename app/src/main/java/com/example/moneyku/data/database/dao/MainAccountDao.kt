package com.example.moneyku.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.moneyku.data.database.entity.MainAccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MainAccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMainAccount(account: MainAccountEntity)

    @Update
    suspend fun updateMainAccount(account: MainAccountEntity)

    @Delete
    suspend fun deleteMainAccount(account: MainAccountEntity)

    @Query("SELECT * FROM main_accounts WHERE user_id = :userId LIMIT 1")
    fun getMainAccountByUserId(userId: String): Flow<MainAccountEntity?>
}
