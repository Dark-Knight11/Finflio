package com.finflio.feature_stats.presentation.util

enum class GraphFilter {
    DUMMY
    ;
    enum class YMW {
        WEEKLY,
        MONTHLY,
        YEARLY
    }

    enum class ChartStyle {
        BARCHART,
        LINECHART
    }

    enum class Type {
        INCOME,
        COMBINED,
        EXPENSE
    }
}
