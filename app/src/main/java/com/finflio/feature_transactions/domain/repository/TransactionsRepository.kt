package com.finflio.feature_transactions.domain.repository

import androidx.paging.PagingData
import com.finflio.feature_transactions.data.models.local.MonthTotalEntity
import com.finflio.feature_transactions.data.models.local.TransactionEntity
import com.finflio.feature_transactions.data.models.local.UnsettledTransactionEntity
import com.finflio.feature_transactions.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {

    fun getTransactions(month: String): Flow<PagingData<Pair<TransactionEntity, Int>>>

    suspend fun getMonthTotal(month: String): MonthTotalEntity

    fun getUnsettledTransaction(): Flow<PagingData<UnsettledTransactionEntity>>

    suspend fun getTransaction(id: String): Transaction

    suspend fun getUnsettledTransaction(id: String): Transaction

    suspend fun deleteTransaction(transaction: Transaction)

    suspend fun updateTransaction(transaction: Transaction)

    suspend fun addTransaction(transaction: Transaction)

    suspend fun deleteImage(imageID: String?)
}
