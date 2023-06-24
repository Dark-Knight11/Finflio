package com.finflio.feature_transactions.domain.use_case

import com.finflio.core.data.network.resource.Resource
import com.finflio.feature_transactions.data.models.remote.DeleteTransactionResponse
import com.finflio.feature_transactions.domain.repository.TransactionsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class DeleteTransactionUseCase @Inject constructor(
    private val repo: TransactionsRepository
) {
    suspend operator fun invoke(transactionId: String): Flow<Resource<DeleteTransactionResponse>> {
        return repo.deleteTransaction(transactionId)
    }
}
