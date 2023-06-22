package com.finflio.feature_transactions.data.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "unsettled_transaction_remote_keys")
data class UnsettledTransactionRemoteKeys(
    @PrimaryKey
    val id: String,
    val prevPage: Int?,
    val nextPage: Int?
)