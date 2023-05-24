package com.finflio.feature_stats.data.di

import com.finflio.core.domain.repository.TransactionsRepository
import com.finflio.feature_stats.domain.use_case.GetCustomRangeData
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
    fun provideGetCustomRangeData(repository: TransactionsRepository): GetCustomRangeData {
        return GetCustomRangeData(repository)
    }
}
