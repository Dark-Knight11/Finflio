package com.finflio.feature_authentication.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val status: Int,
    val message: String,
    val token: String? = null
)
