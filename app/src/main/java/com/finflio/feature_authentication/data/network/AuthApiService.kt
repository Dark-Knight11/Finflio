package com.finflio.feature_authentication.data.network

import com.finflio.feature_authentication.data.models.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/register")
    suspend fun register(
        @Body postParams: Map<String, String>
    ): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(
        @Body postParams: Map<String, String>
    ): Response<AuthResponse>
}