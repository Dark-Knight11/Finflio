package com.finflio.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JwtToken(
    @SerialName("aud")
    val aud: String,
    @SerialName("exp")
    val exp: Int,
    @SerialName("iss")
    val iss: String,
    @SerialName("sub")
    val sub: String,
    @SerialName("userId")
    val userId: String
)