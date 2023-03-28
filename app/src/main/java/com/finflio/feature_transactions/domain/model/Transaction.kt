package com.finflio.feature_transactions.domain.model

import java.time.LocalDateTime

data class Transaction(
    val transactionId: Int = 0,
    val timestamp: LocalDateTime,
    val type: String,
    val category: String,
    val paymentMethod: String,
    val description: String,
    val amount: Float,
    val attachment: String? = null,
    val from: String? = null,
    val to: String? = null
)