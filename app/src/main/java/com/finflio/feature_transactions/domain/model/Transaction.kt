package com.finflio.feature_transactions.domain.model

import android.os.Parcelable
import java.time.LocalDateTime
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transaction(
    val transactionId: String,
    val userId: String,
    val timestamp: LocalDateTime,
    val type: String,
    val category: String,
    val paymentMethod: String,
    val description: String,
    val amount: Float,
    val attachment: String? = null,
    val from: String? = null,
    val to: String? = null
) : Parcelable