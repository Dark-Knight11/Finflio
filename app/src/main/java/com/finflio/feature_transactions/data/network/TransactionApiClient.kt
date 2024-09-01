package com.finflio.feature_transactions.data.network

import com.finflio.core.data.network.BaseApiClient
import com.finflio.core.data.util.UtilityMethods
import com.finflio.feature_transactions.data.models.remote.TransactionPostRequest
import javax.inject.Inject
import kotlinx.serialization.json.Json

class TransactionApiClient @Inject constructor(
    private val apiService: TransactionApiService,
    utilityMethods: UtilityMethods
) : BaseApiClient(utilityMethods) {

    suspend fun getTransactions(page: Int, month: String, year: Int? = null) = getResult {
        apiService.getTransactions(page, month, year)
    }

    suspend fun getUnsettledTransactions(page: Int) = getResult {
        apiService.getUnsettledTransactions(page)
    }

    suspend fun createTransaction(transactionPostRequest: TransactionPostRequest) =
        makePostRequest {
            apiService.createTransaction(
                Json.encodeToJsonElement(
                    TransactionPostRequest.serializer(),
                    transactionPostRequest
                )
            )
        }

    suspend fun deleteTransaction(transactionId: String) = getResult {
        apiService.deleteTransaction(transactionId)
    }

    suspend fun updateTransaction(
        transactionId: String,
        transactionPostRequest: TransactionPostRequest
    ) = makePostRequest {
        apiService.updateTransaction(
            transactionId,
            Json.encodeToJsonElement(TransactionPostRequest.serializer(), transactionPostRequest)
        )
    }
}