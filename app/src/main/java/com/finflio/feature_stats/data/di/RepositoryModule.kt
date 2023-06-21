package com.finflio.feature_stats.data.di

import com.finflio.feature_stats.data.network.StatsApiClient
import com.finflio.feature_stats.data.repository.StatsRepositoryImpl
import com.finflio.feature_stats.domain.repository.StatsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideStatsRepository(apiClient: StatsApiClient): StatsRepository {
        return StatsRepositoryImpl(apiClient)
    }
}