package com.finflio.feature_stats.data.di

import com.finflio.feature_stats.domain.repository.StatsRepository
import com.finflio.feature_stats.domain.use_case.GetStatsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StatsModule {

    @Provides
    @Singleton
    fun provideGetStatsUseCase(repository: StatsRepository): GetStatsUseCase {
        return GetStatsUseCase(repository)
    }
}
