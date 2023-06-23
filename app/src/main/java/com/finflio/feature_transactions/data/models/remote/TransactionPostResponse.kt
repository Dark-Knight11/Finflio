package com.finflio.feature_transactions.data.models.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionPostResponse(
    @SerialName("message")
    val message: String,
    @SerialName("status")
    val status: Int,
    @SerialName("transaction")
    val transaction: TransactionDTO
)