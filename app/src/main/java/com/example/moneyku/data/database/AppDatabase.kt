package com.example.moneyku.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moneyku.data.database.converter.Converters
import com.example.moneyku.data.database.dao.CategoryDao
import com.example.moneyku.data.database.dao.GroupTransactionDao
import com.example.moneyku.data.database.dao.MainAccountDao
import com.example.moneyku.data.database.dao.NotificationDao
import com.example.moneyku.data.database.dao.SplitTransactionDao
import com.example.moneyku.data.database.dao.TransactionDao
import com.example.moneyku.data.database.dao.UserDao
import com.example.moneyku.data.database.entity.CategoryEntity
import com.example.moneyku.data.database.entity.GroupTransactionEntity
import com.example.moneyku.data.database.entity.MainAccountEntity
import com.example.moneyku.data.database.entity.NotificationEntity
import com.example.moneyku.data.database.entity.SplitTransactionEntity
import com.example.moneyku.data.database.entity.TransactionEntity
import com.example.moneyku.data.database.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        MainAccountEntity::class,
        CategoryEntity::class,
        TransactionEntity::class,
        GroupTransactionEntity::class,
        SplitTransactionEntity::class,
        NotificationEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun mainAccountDao(): MainAccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun groupTransactionDao(): GroupTransactionDao
    abstract fun splitTransactionDao(): SplitTransactionDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "moneyku_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
