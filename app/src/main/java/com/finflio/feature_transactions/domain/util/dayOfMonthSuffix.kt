package com.finflio.feature_transactions.domain.util

fun getDayOfMonthSuffix(day: Int): String {
    return when (day) {
        1, 21, 31 -> "st"
        2, 22 -> "nd"
        3, 23 -> "rd"
        else -> "th"
    }
}