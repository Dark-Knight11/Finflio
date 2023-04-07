package com.finflio.core.data.repository

import com.finflio.core.data.data_source.TransactionDao
import com.finflio.core.data.mapper.toTransaction
import com.finflio.core.data.mapper.toTransactionEntity
import com.finflio.core.domain.model.Transaction
import com.finflio.core.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionsRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionsRepository {

    override fun getTransactions(): Flow<List<Transaction>> {
        return dao.getTransactions().map {
            it.map { transactionEntity ->
                transactionEntity.toTransaction()
            }
        }
    }

    override suspend fun getTransaction(id: Int): Transaction {
        return dao.getTransaction(id).toTransaction()
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        dao.deleteTransaction(transaction.toTransactionEntity())
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        dao.updateTransaction(transaction.toTransactionEntity())
    }

    override suspend fun addTransaction(transaction: Transaction) {
        dao.addTransaction(transaction.toTransactionEntity())
    }
}