package com.finflio.feature_stats.domain.model

import com.finflio.feature_stats.data.models.MonthlyData
import com.finflio.feature_stats.data.models.WeeklyData
import com.finflio.feature_stats.data.models.YearlyData

data class StatsData(
    val monthlyData: List<MonthlyData>,
    val weeklyData: List<WeeklyData>,
    val yearlyData: List<YearlyData>
)