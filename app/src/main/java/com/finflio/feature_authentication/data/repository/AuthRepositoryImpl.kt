package com.finflio.feature_authentication.data.repository

import com.finflio.core.data.model.UserSettings
import com.finflio.core.data.repository.BaseRepo
import com.finflio.core.data.util.SessionManager
import com.finflio.feature_authentication.data.network.AuthApiClient
import com.finflio.feature_authentication.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authApiClient: AuthApiClient,
    private val sessionManager: SessionManager
) : BaseRepo(), AuthRepository {

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ) = makeRequest {
        authApiClient.register(name, email, password)
    }

    override suspend fun login(email: String, password: String) = requestAndSave(
        networkCall = {
            authApiClient.login(email, password)
        },
        saveCallResult = {
            if (it.status == 200) {
                val user = UserSettings(
                    email = email,
                    token = it.token
                )
                sessionManager.saveUserData(user)
            }
        }
    )
}