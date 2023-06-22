package com.finflio.feature_transactions.data.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_remote_keys")
data class TransactionRemoteKeys(
    @PrimaryKey
    val id: String,
    val prevPage: Int?,
    val nextPage: Int?
)