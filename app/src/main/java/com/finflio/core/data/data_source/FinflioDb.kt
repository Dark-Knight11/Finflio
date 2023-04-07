package com.finflio.core.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.finflio.core.data.model.TransactionEntity
import com.finflio.core.data.util.Converters

@Database(
    entities = [TransactionEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class FinflioDb(): RoomDatabase() {
    abstract val transactionDao: TransactionDao
}