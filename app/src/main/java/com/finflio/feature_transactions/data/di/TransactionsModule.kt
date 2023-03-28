package com.finflio.feature_transactions.data.di

import com.finflio.core.data.FinflioDb
import com.finflio.feature_transactions.data.data_source.TransactionDao
import com.finflio.feature_transactions.data.repository.TransactionsRepositoryImpl
import com.finflio.feature_transactions.domain.repository.TransactionsRepository
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
    fun provideTransactionDao(finflioDb: FinflioDb): TransactionDao {
        return finflioDb.transactionDao
    }

    @Provides
    @Singleton
    fun provideTransactionRepo(transactionDao: TransactionDao): TransactionsRepository {
        return TransactionsRepositoryImpl(transactionDao)
    }

    @Provides
    @Singleton
    fun provideTransactionsUseCases(repository: TransactionsRepository): TransactionUseCases {
        return TransactionUseCases(
            getTransactionsUseCase = GetTransactionsUseCase(repository),
            getTransactionUseCase = GetTransactionUseCase(repository),
            addTransactionUseCase = AddTransactionUseCase(repository),
            updateTransactionUseCase = UpdateTransactionUseCase(repository),
            deleteTransactionUseCase = DeleteTransactionUseCase(repository),
            getMonthTotalUseCase = GetMonthTotalUseCase(repository)
        )
    }
}
