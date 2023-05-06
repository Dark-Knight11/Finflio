package com.finflio.feature_transactions.data.di

import com.finflio.core.domain.repository.TransactionsRepository
import com.finflio.core.presentation.util.UriPathFinder
import com.finflio.feature_transactions.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object TransactionsModule {

    @Provides
    @Singleton
    fun provideTransactionsUseCases(
        repository: TransactionsRepository,
        uriPathFinder: UriPathFinder
    ): TransactionUseCases {
        return TransactionUseCases(
            getTransactionsUseCase = GetTransactionsUseCase(repository),
            getTransactionUseCase = GetTransactionUseCase(repository),
            addTransactionUseCase = AddTransactionUseCase(repository),
            updateTransactionUseCase = UpdateTransactionUseCase(repository),
            deleteTransactionUseCase = DeleteTransactionUseCase(repository),
            deleteImageUseCase = DeleteImageUseCase(repository),
            getUnsettledTransactionsUseCase = GetUnsettledTransactionsUseCase(repository),
            getImagePathUseCase = GetImagePathUseCase(uriPathFinder)
        )
    }
}
