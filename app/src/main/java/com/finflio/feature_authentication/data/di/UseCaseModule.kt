package com.finflio.feature_authentication.data.di

import com.finflio.feature_authentication.domain.repository.AuthRepository
import com.finflio.feature_authentication.domain.use_case.LoginUseCase
import com.finflio.feature_authentication.domain.use_case.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideLoginUseCase(authRepo: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepo)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(authRepo: AuthRepository): RegisterUseCase {
        return RegisterUseCase(authRepo)
    }
}