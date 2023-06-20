package com.finflio.feature_authentication.data.network

import com.finflio.core.data.network.BaseApiClient
import com.finflio.core.data.network.UtilityMethods

class AuthApiClient(
    private val authApiService: AuthApiService,
    utilityMethods: UtilityMethods
) : BaseApiClient(utilityMethods) {

    suspend fun register(
        name: String,
        email: String,
        password: String
    ) = makePostRequest {
        val map = HashMap<String, String>()
        map["name"] = name
        map["email"] = email
        map["password"] = password
        authApiService.register(map)
    }

    suspend fun login(
        email: String,
        password: String
    ) = makePostRequest {
        val map = HashMap<String, String>()
        map["email"] = email
        map["password"] = password
        authApiService.login(map)
    }
}