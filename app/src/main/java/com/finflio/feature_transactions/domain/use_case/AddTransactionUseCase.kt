package com.finflio.feature_transactions.domain.use_case

import com.finflio.core.data.network.resource.Resource
import com.finflio.feature_transactions.data.models.remote.TransactionPostRequest
import com.finflio.feature_transactions.data.models.remote.TransactionPostResponse
import com.finflio.feature_transactions.domain.repository.TransactionsRepository
import com.finflio.feature_transactions.domain.util.InvalidTransactionException
import com.finflio.feature_transactions.domain.util.errors
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class AddTransactionUseCase @Inject constructor(
    private val repository: TransactionsRepository
) {
    @Throws(InvalidTransactionException::class)
    suspend operator fun invoke(transactionPostRequest: TransactionPostRequest): Flow<Resource<TransactionPostResponse>> {
        errors(transactionPostRequest)
        return repository.addTransaction(transactionPostRequest)
    }
}
