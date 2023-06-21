package com.finflio.feature_stats.data.repository

import com.finflio.core.data.network.resource.Resource
import com.finflio.core.data.repository.BaseRepo
import com.finflio.feature_stats.data.models.StatsDTO
import com.finflio.feature_stats.data.network.StatsApiClient
import com.finflio.feature_stats.domain.repository.StatsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class StatsRepositoryImpl @Inject constructor(
    private val apiClient: StatsApiClient
) : BaseRepo(), StatsRepository {

    override suspend fun getStats(): Flow<Resource<StatsDTO>> = makeRequest {
        apiClient.getStats()
    }
}