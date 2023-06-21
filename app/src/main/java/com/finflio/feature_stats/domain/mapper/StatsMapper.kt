package com.finflio.feature_stats.domain.mapper

import com.finflio.feature_stats.data.models.Stats
import com.finflio.feature_stats.domain.model.StatsData

fun Stats.toStatsData(): StatsData {
    return StatsData(
        monthlyData,
        weeklyData,
        yearlyData
    )
}