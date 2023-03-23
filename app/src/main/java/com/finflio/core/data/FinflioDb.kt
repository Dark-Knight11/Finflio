package com.finflio.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.finflio.core.data.util.Converters
import com.finflio.feature_transactions.data.data_source.TransactionDao
import com.finflio.feature_transactions.data.data_source.TransactionEntity

@Database(
    entities = [TransactionEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class FinflioDb(): RoomDatabase() {
    abstract val transactionDao: TransactionDao
}