package com.finflio.core.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.finflio.feature_transactions.data.dao.TransactionDao
import com.finflio.feature_transactions.data.dao.TransactionRemoteKeysDao
import com.finflio.feature_transactions.data.models.local.TransactionEntity
import com.finflio.feature_transactions.data.models.local.TransactionRemoteKeys

@Database(
    entities = [
        TransactionEntity::class,
        TransactionRemoteKeys::class
    ],
    version = 1,
    exportSchema = true
)
abstract class FinflioDb() : RoomDatabase() {
    abstract val transactionDao: TransactionDao
    abstract val transactionRemoteKeysDao: TransactionRemoteKeysDao
}
