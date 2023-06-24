package com.finflio.feature_transactions.data.models.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteTransactionResponse(
    @SerialName("status")
    val status: Int,
    @SerialName("message")
    val message: String
)