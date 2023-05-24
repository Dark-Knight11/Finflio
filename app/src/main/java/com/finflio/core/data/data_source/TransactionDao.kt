package com.finflio.core.data.data_source

import androidx.room.*
import com.finflio.core.data.model.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransaction(transactionEntity: TransactionEntity)

    @Query("Select * from transactions")
    fun getTransactions(): Flow<List<TransactionEntity>>

    @Query("Select * from transactions where transactionId Like :id")
    suspend fun getTransaction(id: Int): TransactionEntity

    @Delete
    suspend fun deleteTransaction(transactionEntity: TransactionEntity)

    @Update
    suspend fun updateTransaction(transactionEntity: TransactionEntity)
}
