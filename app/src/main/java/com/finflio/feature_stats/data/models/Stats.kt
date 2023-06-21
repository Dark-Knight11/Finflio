package com.finflio.feature_stats.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Stats(
    @SerialName("monthlyData")
    val monthlyData: List<MonthlyData>,
    @SerialName("weeklyData")
    val weeklyData: List<WeeklyData>,
    @SerialName("yearlyData")
    val yearlyData: List<YearlyData>
)