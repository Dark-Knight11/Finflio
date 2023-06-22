package com.finflio.feature_transactions.data.network

import com.finflio.core.data.network.BaseApiClient
import com.finflio.core.data.util.UtilityMethods
import javax.inject.Inject

class TransactionApiClient @Inject constructor(
    private val apiService: TransactionApiService,
    utilityMethods: UtilityMethods
) : BaseApiClient(utilityMethods) {

    suspend fun getTransactions(page: Int, month: String) = getResult {
        apiService.getTransactions(page, month)
    }

    suspend fun getUnsettledTransactions(page: Int) = getResult {
        apiService.getUnsettledTransactions(page)
    }
}