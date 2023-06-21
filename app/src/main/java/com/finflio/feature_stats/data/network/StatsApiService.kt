package com.finflio.feature_stats.data.network

import com.finflio.feature_stats.data.models.StatsDTO
import retrofit2.Response
import retrofit2.http.GET

interface StatsApiService {

    @GET("transaction/stats")
    suspend fun getStats(): Response<StatsDTO>
}