package com.finflio.feature_stats.domain.util

import com.finflio.core.domain.model.Transaction
import com.finflio.feature_stats.domain.model.Summary
import com.finflio.feature_stats.presentation.util.GraphFilter
import com.patrykandpatrick.vico.core.extension.sumByFloat
import java.time.LocalDate

fun calculateMonthlyData(
    currentDate: LocalDate,
    transactionList: List<Transaction>,
    typeFilter: GraphFilter.Type
): List<Summary> {
    if (typeFilter != GraphFilter.Type.COMBINED) {
        val summaryList = (1..currentDate.month.maxLength()).map { date ->
            val total = transactionList
                .filter { it.timestamp.year == currentDate.year && it.timestamp.month == currentDate.month }
                .filter {
                    it.type == typeFilter.name.lowercase().replaceFirstChar { char ->
                        char.titlecase()
                    }
                }
                .filter { it.timestamp.dayOfMonth == date }
                .sumByFloat { it.amount }
            if (typeFilter == GraphFilter.Type.EXPENSE) {
                Summary(total, 0f)
            } else {
                Summary(0f, total)
            }
        }
        return summaryList
    } else {
        val summaryList = (1..currentDate.month.maxLength()).map { date ->
            val (expenses, income) = transactionList
                .filter { it.timestamp.year == currentDate.year && it.timestamp.month == currentDate.month }
                .filter { it.type == "Expense" || it.type == "Income" }
                .filter { it.timestamp.dayOfMonth == date }
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
