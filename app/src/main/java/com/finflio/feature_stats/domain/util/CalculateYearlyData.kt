package com.finflio.feature_stats.domain.util

import com.finflio.core.domain.model.Transaction
import com.finflio.feature_stats.domain.model.Summary
import com.finflio.feature_stats.presentation.util.GraphFilter
import com.patrykandpatrick.vico.core.extension.sumByFloat
import java.time.LocalDate
import java.time.Month

fun calculateYearlyData(
    transactionList: List<Transaction>,
    typeFilter: GraphFilter.Type
): List<Summary> {
    val currentYear = LocalDate.now().year
    if (typeFilter != GraphFilter.Type.COMBINED) {
        val summaryList = Month.values().map { month ->
            val total = transactionList
                .filter {
                    it.timestamp.month == month && it.timestamp.year == currentYear
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
        val summaryList = Month.values().map { month ->
            val (expenses, income) = transactionList
                .filter {
                    it.timestamp.month == month && it.timestamp.year == currentYear
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
