package com.finflio.feature_transactions.data.di

import com.finflio.core.data.data_source.FinflioDb
import com.finflio.feature_transactions.data.network.TransactionApiClient
import com.finflio.feature_transactions.data.repository.TransactionsRepositoryImpl
import com.finflio.feature_transactions.domain.repository.TransactionsRepository
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
    fun provideTransactionRepo(
        finflioDb: FinflioDb,
        apiClient: TransactionApiClient
    ): TransactionsRepository {
        return TransactionsRepositoryImpl(apiClient, finflioDb)
    }
}