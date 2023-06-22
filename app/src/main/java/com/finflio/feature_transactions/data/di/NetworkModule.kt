package com.finflio.feature_transactions.data.di

import com.finflio.core.data.util.UtilityMethods
import com.finflio.feature_transactions.data.network.TransactionApiClient
import com.finflio.feature_transactions.data.network.TransactionApiService
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
    fun provideTransactionApiService(retrofit: Retrofit): TransactionApiService {
        return retrofit.create(TransactionApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTransactionApiClient(
        transactionApiService: TransactionApiService,
        utilityMethods: UtilityMethods
    ): TransactionApiClient {
        return TransactionApiClient(transactionApiService, utilityMethods)
    }
}