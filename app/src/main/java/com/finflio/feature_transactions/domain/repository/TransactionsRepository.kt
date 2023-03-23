package com.finflio.feature_transactions.domain.repository

import com.finflio.feature_transactions.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {

    fun getTransactions(): Flow<List<Transaction>>

    suspend fun getTransaction(id: Int): Transaction

    suspend fun deleteTransaction(transaction: Transaction)

    suspend fun updateTransaction(transaction: Transaction)

    suspend fun addTransaction(transaction: Transaction)
}