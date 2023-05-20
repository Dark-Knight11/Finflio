package com.finflio.feature_stats.domain.model

import com.finflio.feature_stats.presentation.util.GraphFilter

data class SelectedChartFilters(
    val type: GraphFilter.Type = GraphFilter.Type.COMBINED,
    val ymw: GraphFilter.YMW = GraphFilter.YMW.WEEKLY
)
