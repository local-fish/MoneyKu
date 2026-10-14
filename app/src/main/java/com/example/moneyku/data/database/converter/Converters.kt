package com.example.moneyku.data.database.converter

import androidx.room.TypeConverter
import com.example.moneyku.data.database.entity.NotificationType
import com.example.moneyku.data.database.entity.SplitTransactionStatus
import com.example.moneyku.data.database.entity.TransactionType
import java.math.BigDecimal

class Converters {
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? = value?.toPlainString()

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? = value?.let { BigDecimal(it) }

    @TypeConverter
    fun fromTransactionType(value: TransactionType?): String? = value?.name

    @TypeConverter
    fun toTransactionType(value: String?): TransactionType? = value?.let { TransactionType.valueOf(it) }

    @TypeConverter
    fun fromSplitTransactionStatus(value: SplitTransactionStatus?): String? = value?.name

    @TypeConverter
    fun toSplitTransactionStatus(value: String?): SplitTransactionStatus? = value?.let { SplitTransactionStatus.valueOf(it) }

    @TypeConverter
    fun fromNotificationType(value: NotificationType?): String? = value?.name

    @TypeConverter
    fun toNotificationType(value: String?): NotificationType? = value?.let { NotificationType.valueOf(it) }
}
