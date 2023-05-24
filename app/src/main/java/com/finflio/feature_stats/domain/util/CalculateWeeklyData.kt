package com.finflio.feature_stats.domain.util

import com.finflio.core.domain.model.Transaction
import com.finflio.feature_stats.domain.model.Summary
import com.finflio.feature_stats.presentation.util.GraphFilter
import com.patrykandpatrick.vico.core.extension.sumByFloat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

fun calculateWeeklyData(
    currentDate: LocalDate,
    transactionList: List<Transaction>,
    typeFilter: GraphFilter.Type
): List<Summary> {
    val weekFields = WeekFields.of(Locale.FRANCE)
    val currentWeek = currentDate.get(weekFields.weekOfWeekBasedYear())
    if (typeFilter != GraphFilter.Type.COMBINED) {
        val summaryList = DayOfWeek.values().map { dayOfWeek ->
            val total = transactionList
                .filter {
                    it.timestamp.get(weekFields.weekOfWeekBasedYear()) == currentWeek &&
                        it.timestamp.dayOfWeek == dayOfWeek
                }
                .filter {
                    it.type == typeFilter.name.lowercase().replaceFirstChar { char ->
                        char.titlecase()
                    }
                }
                .sumByFloat { it.amount }

            if (typeFilter == GraphFilter.Type.EXPENSE) {
                Summary(total, 0f)
            } else {
                Summary(0f, total)
            }
        }
        return summaryList
    } else {
        val summaryList = DayOfWeek.values().map { dayOfWeek ->
            val (expenses, income) = transactionList
                .filter {
                    it.timestamp.get(weekFields.weekOfWeekBasedYear()) == currentWeek &&
                        it.timestamp.dayOfWeek == dayOfWeek
                }
                .filter { it.type == "Expense" || it.type == "Income" }
                .fold(0.0f to 0.0f) { acc, transaction ->
                    if (transaction.type == "Expense") {
                        acc.copy(first = acc.first + transaction.amount)
                    } else {
                        acc.copy(second = acc.second + transaction.amount)
                    }
                }
            Summary(expenses, income)
        }
        return summaryList
    }
}
