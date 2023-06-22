package com.finflio.feature_transactions.data.models.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionDTO(
    @SerialName("timestamp")
    val timestamp: Long,
    @SerialName("type")
    val type: String,
    @SerialName("category")
    val category: String,
    @SerialName("paymentMethod")
    val paymentMethod: String,
    @SerialName("description")
    val description: String,
    @SerialName("amount")
    val amount: Float,
    @SerialName("attachment")
    val attachment: String? = null,
    @SerialName("from")
    val from: String? = null,
    @SerialName("to")
    val to: String? = null,
    @SerialName("userId")
    val userId: String,
    @SerialName("id")
    val id: String
)