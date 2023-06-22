package com.finflio.feature_transactions.data.models.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionResponse(
    @SerialName("message")
    val message: String,
    @SerialName("monthTotal")
    val monthTotal: Int? = null,
    @SerialName("status")
    val status: Int,
    @SerialName("totalPages")
    val totalPages: Int,
    @SerialName("transactions")
    val transactions: List<TransactionDTO>
)