package com.finflio.feature_transactions.domain.use_case

import com.finflio.core.domain.model.Transaction
import com.finflio.core.domain.repository.TransactionsRepository
import com.finflio.feature_transactions.domain.util.InvalidTransactionException
import com.finflio.feature_transactions.domain.util.errors
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val repository: TransactionsRepository
) {
    @Throws(InvalidTransactionException::class)
    suspend operator fun invoke(transaction: Transaction): Boolean {
        errors(transaction)
        repository.addTransaction(transaction)
        return true
    }
}
