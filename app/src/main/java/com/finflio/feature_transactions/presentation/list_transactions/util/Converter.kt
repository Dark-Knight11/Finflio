package com.finflio.feature_transactions.presentation.list_transactions.util

import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoField
import java.util.*

fun shortToLongMonth(month: String): String {
    val formatter = DateTimeFormatter.ofPattern("MMM", Locale.getDefault())
    val longMonth = formatter.parse(month).get(ChronoField.MONTH_OF_YEAR)
    return Month.of(longMonth).getDisplayName(TextStyle.FULL, Locale.getDefault())
}