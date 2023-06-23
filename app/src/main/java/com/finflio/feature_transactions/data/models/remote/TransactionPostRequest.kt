package com.finflio.feature_transactions.data.models.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionPostRequest(
    @SerialName("amount")
    val amount: Float,
    @SerialName("category")
    val category: String,
    @SerialName("description")
    val description: String,
    @SerialName("paymentMethod")
    val paymentMethod: String,
    @SerialName("timestamp")
    val timestamp: Long,
    @SerialName("type")
    val type: String,
    @SerialName("to")
    val to: String? = null,
    @SerialName("from")
    val from: String? = null,
    @SerialName("attachment")
    val attachment: String? = null
)