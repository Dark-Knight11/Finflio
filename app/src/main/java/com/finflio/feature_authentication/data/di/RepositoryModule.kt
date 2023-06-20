package com.finflio.feature_authentication.data.di

import com.finflio.core.data.util.SessionManager
import com.finflio.feature_authentication.data.network.AuthApiClient
import com.finflio.feature_authentication.data.repository.AuthRepositoryImpl
import com.finflio.feature_authentication.domain.repository.AuthRepository
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
    fun provideAuthRepository(
        authApiClient: AuthApiClient,
        sessionManager: SessionManager
    ): AuthRepository {
        return AuthRepositoryImpl(authApiClient, sessionManager)
    }
}