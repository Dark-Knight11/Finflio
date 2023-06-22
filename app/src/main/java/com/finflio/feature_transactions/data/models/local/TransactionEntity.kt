package com.finflio.feature_transactions.data.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey
    val transactionId: String,
    val userId: String,
    val timestamp: Long,
    val type: String,
    val category: String,
    val paymentMethod: String,
    val description: String,
    val amount: Float,
    val attachment: String? = null,
    val from: String? = null,
    val to: String? = null
)
