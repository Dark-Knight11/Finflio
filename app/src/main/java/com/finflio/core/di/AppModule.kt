package com.finflio.core.di

import android.app.Application
import androidx.room.Room
import com.finflio.core.data.FinflioDb
import com.finflio.core.data.util.Converters
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
}