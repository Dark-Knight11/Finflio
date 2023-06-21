package com.finflio.feature_stats.domain.repository

import com.finflio.core.data.network.resource.Resource
import com.finflio.feature_stats.data.models.StatsDTO
import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    suspend fun getStats(): Flow<Resource<StatsDTO>>
}