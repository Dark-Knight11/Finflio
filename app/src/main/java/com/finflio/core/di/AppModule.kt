package com.finflio.core.di

import android.app.Application
import androidx.room.Room
import com.finflio.core.data.FinflioDb
import com.finflio.core.data.util.Converters
import com.finflio.feature_transactions.data.data_source.TransactionDao
import com.finflio.feature_transactions.data.repository.TransactionsRepositoryImpl
import com.finflio.feature_transactions.domain.repository.TransactionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFinflioDatabase(app: Application): FinflioDb {
        val converters = Converters()
        return Room.databaseBuilder(
            context = app,
            klass = FinflioDb::class.java,
            name = "FinflioDB"
        ).addTypeConverter(converters)
            .build()
    }

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
}