package com.finflio.feature_transactions.data.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.finflio.feature_transactions.data.models.local.TransactionEntity

@Dao
interface TransactionDao {

    @Upsert
    suspend fun upsertTransactions(transactionEntity: List<TransactionEntity>)

    @Query("Delete from transactions")
    suspend fun deleteAllTransactions()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransaction(transactionEntity: TransactionEntity)

    @Query("Select * from transactions")
    fun getTransactions(): PagingSource<Int, TransactionEntity>

    @Query("Select * from transactions where transactionId Like :id")
    suspend fun getTransaction(id: String): TransactionEntity

    @Delete
    suspend fun deleteTransaction(transactionEntity: TransactionEntity)

    @Update
    suspend fun updateTransaction(transactionEntity: TransactionEntity)
}
