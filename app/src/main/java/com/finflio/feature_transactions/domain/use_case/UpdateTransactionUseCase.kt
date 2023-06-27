package com.finflio.feature_transactions.domain.use_case

import com.finflio.core.data.network.resource.Resource
import com.finflio.feature_transactions.data.models.remote.TransactionPostRequest
import com.finflio.feature_transactions.data.models.remote.TransactionPostResponse
import com.finflio.feature_transactions.domain.repository.TransactionsRepository
import com.finflio.feature_transactions.domain.util.errors
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class UpdateTransactionUseCase @Inject constructor(
    private val repo: TransactionsRepository
) {
    suspend operator fun invoke(
        transactionId: String,
        transactionPostRequest: TransactionPostRequest,
        file: File? = null
    ): Flow<Resource<TransactionPostResponse>> {
        errors(transactionPostRequest)
        return repo.updateTransaction(transactionId, transactionPostRequest, file)
    }
}
