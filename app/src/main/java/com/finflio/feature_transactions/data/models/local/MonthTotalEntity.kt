package com.finflio.feature_transactions.data.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "month_total")
data class MonthTotalEntity(
    @PrimaryKey
    val month: String,
    val total: Int
)
