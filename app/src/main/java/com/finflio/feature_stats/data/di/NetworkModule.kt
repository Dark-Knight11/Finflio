package com.finflio.feature_stats.data.di

import com.finflio.core.data.util.UtilityMethods
import com.finflio.feature_stats.data.network.StatsApiClient
import com.finflio.feature_stats.data.network.StatsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideStatsApiService(retrofit: Retrofit): StatsApiService {
        return retrofit.create(StatsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideStatsApiClient(
        statsApiService: StatsApiService,
        utilityMethods: UtilityMethods
    ): StatsApiClient {
        return StatsApiClient(statsApiService, utilityMethods)
    }
}