package com.finflio.core.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.finflio.feature_transactions.data.dao.TransactionDao
import com.finflio.feature_transactions.data.dao.TransactionRemoteKeysDao
import com.finflio.feature_transactions.data.dao.UnsettledTransactionDao
import com.finflio.feature_transactions.data.dao.UnsettledTransactionRemoteKeysDao
import com.finflio.feature_transactions.data.models.local.TransactionEntity
import com.finflio.feature_transactions.data.models.local.TransactionRemoteKeys
import com.finflio.feature_transactions.data.models.local.UnsettledTransactionEntity
import com.finflio.feature_transactions.data.models.local.UnsettledTransactionRemoteKeys

@Database(
    entities = [
        TransactionEntity::class,
        TransactionRemoteKeys::class,
        UnsettledTransactionEntity::class,
        UnsettledTransactionRemoteKeys::class
    ],
    version = 3,
    exportSchema = true
)
abstract class FinflioDb() : RoomDatabase() {
    abstract val transactionDao: TransactionDao
    abstract val transactionRemoteKeysDao: TransactionRemoteKeysDao
    abstract val unsettledTransactionDao: UnsettledTransactionDao
    abstract val unsettledTransactionRemoteKeysDao: UnsettledTransactionRemoteKeysDao
}
