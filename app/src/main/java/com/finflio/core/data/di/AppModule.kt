package com.finflio.core.data.di

import android.app.Application
import androidx.room.Room
import com.finflio.core.data.data_source.FinflioDb
import com.finflio.core.data.data_source.TransactionDao
import com.finflio.core.data.repository.TransactionsRepositoryImpl
import com.finflio.core.data.util.Converters
import com.finflio.core.domain.repository.TransactionsRepository
import com.finflio.core.presentation.util.UriPathFinder
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
        )
            .addTypeConverter(converters)
            .fallbackToDestructiveMigration()
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

    @Provides
    @Singleton
    fun provideUriPathFinder(): UriPathFinder {
        return UriPathFinder()
    }
}
