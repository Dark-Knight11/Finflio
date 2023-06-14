package com.finflio.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    val email: String? = null,
    val password: String? = null
)