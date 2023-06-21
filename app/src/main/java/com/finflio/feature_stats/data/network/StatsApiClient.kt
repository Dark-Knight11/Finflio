package com.finflio.feature_stats.data.network

import com.finflio.core.data.network.BaseApiClient
import com.finflio.core.data.util.UtilityMethods

class StatsApiClient(
    private val apiService: StatsApiService,
    utilityMethods: UtilityMethods
) : BaseApiClient(utilityMethods) {

    suspend fun getStats() = getResult {
        apiService.getStats()
    }
}