package com.finflio.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CommonErrorResponse(
    val status: Int,
    val message: String
)