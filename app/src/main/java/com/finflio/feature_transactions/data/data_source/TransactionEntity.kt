package com.finflio.feature_transactions.data.data_source

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    var transactionId: Int = 0,
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
