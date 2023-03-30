package com.finflio.feature_transactions.domain.use_case

import com.finflio.feature_transactions.domain.model.Transaction
import com.finflio.feature_transactions.domain.repository.TransactionsRepository
import com.finflio.feature_transactions.domain.util.errors
import javax.inject.Inject

class UpdateTransactionUseCase @Inject constructor(
    private val repo: TransactionsRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        errors(transaction)
        repo.updateTransaction(transaction)
    }
}