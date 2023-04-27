package com.finflio.core.domain.repository

import com.finflio.core.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {

    fun getTransactions(): Flow<List<Transaction>>

    suspend fun getTransaction(id: Int): Transaction

    suspend fun deleteTransaction(transaction: Transaction)

    suspend fun updateTransaction(transaction: Transaction)

    suspend fun addTransaction(transaction: Transaction)

    suspend fun deleteImage(imageID: String?)
}