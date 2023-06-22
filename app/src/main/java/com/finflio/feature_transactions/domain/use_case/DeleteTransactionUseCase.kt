package com.finflio.feature_transactions.domain.use_case

import com.finflio.feature_transactions.domain.model.Transaction
import com.finflio.feature_transactions.domain.repository.TransactionsRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val repo: TransactionsRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        repo.deleteTransaction(transaction)
    }
}
