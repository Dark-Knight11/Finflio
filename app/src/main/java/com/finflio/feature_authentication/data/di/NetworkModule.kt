package com.finflio.feature_authentication.data.di

import com.finflio.core.data.util.UtilityMethods
import com.finflio.feature_authentication.data.network.AuthApiClient
import com.finflio.feature_authentication.data.network.AuthApiService
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
    fun provideApiClient(authApiService: AuthApiService, utilityMethods: UtilityMethods): AuthApiClient {
        return AuthApiClient(authApiService, utilityMethods)
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }
}