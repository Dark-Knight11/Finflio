package com.finflio.feature_transactions.domain.use_case

import com.finflio.feature_transactions.domain.model.Transaction
import com.finflio.feature_transactions.domain.repository.TransactionsRepository
import javax.inject.Inject

class GetTransactionUseCase @Inject constructor(
    private val repository: TransactionsRepository
) {
    suspend operator fun invoke(id: String, unsettled: Boolean): Transaction {
        if (unsettled) {
            return repository.getUnsettledTransaction(id)
        }
        return repository.getTransaction(id)
    }
}
