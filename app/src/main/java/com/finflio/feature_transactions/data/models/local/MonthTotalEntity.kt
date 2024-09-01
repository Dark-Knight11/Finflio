package com.finflio.feature_transactions.data.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Year

@Entity(tableName = "month_total")
data class MonthTotalEntity(
    @PrimaryKey
    val month: String,
    val total: Int,
    val year: Int
)
