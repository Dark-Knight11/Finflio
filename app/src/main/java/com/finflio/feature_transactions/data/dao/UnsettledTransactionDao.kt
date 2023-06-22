package com.finflio.feature_transactions.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.finflio.feature_transactions.data.models.local.UnsettledTransactionEntity

@Dao
interface UnsettledTransactionDao {

    @Upsert
    suspend fun upsertUnsettledTransaction(transaction: List<UnsettledTransactionEntity>)

    @Query("Delete from unsettled_transactions")
    suspend fun deleteAllUnsettledTransaction()

    @Query("Select * from unsettled_transactions")
    fun getUnsettledTransactions(): PagingSource<Int, UnsettledTransactionEntity>

    @Query("Select * from unsettled_transactions where transactionId = :id")
    suspend fun getUnsettledTransaction(id: String): UnsettledTransactionEntity
}