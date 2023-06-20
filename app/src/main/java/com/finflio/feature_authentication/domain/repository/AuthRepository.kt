package com.finflio.feature_authentication.domain.repository

import com.finflio.core.data.network.resource.Resource
import com.finflio.feature_authentication.data.models.AuthResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Resource<AuthResponse>>

    suspend fun login(
        email: String,
        password: String
    ): Flow<Resource<AuthResponse>>
}