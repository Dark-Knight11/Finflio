package com.finflio.feature_stats.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MonthlyData(
    @SerialName("date")
    val date: String,
    @SerialName("totalDailyExpense")
    val totalDailyExpense: Int,
    @SerialName("totalDailyIncome")
    val totalDailyIncome: Int
)