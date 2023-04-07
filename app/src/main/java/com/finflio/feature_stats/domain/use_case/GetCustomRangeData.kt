package com.finflio.feature_stats.domain.use_case

import com.finflio.feature_stats.domain.model.Summary
import com.finflio.feature_stats.presentation.util.GraphFilter
import com.finflio.feature_transactions.domain.repository.TransactionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.temporal.WeekFields
import java.util.*
import javax.inject.Inject

class GetCustomRangeData @Inject constructor(
    private val repo: TransactionsRepository
) {
    suspend operator fun invoke(filter: GraphFilter): Flow<List<Summary>> {
        return channelFlow {
            launch(Dispatchers.IO) {
                repo.getTransactions().collectLatest { transactionList ->
                    val currentDate = LocalDate.now()

                    when (filter) {
                        GraphFilter.WEEKLY -> {
                            val weekFields = WeekFields.of(Locale.FRANCE)
                            val currentWeek = currentDate.get(weekFields.weekOfWeekBasedYear())
                            val summaryList = DayOfWeek.values().map { dayOfWeek ->
                                val (expenses, income) = transactionList
                                    .filter {
                                        it.timestamp.get(weekFields.weekOfWeekBasedYear()) == currentWeek
                                                && it.timestamp.dayOfWeek == dayOfWeek
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
                            send(summaryList)
                        }

                        GraphFilter.MONTHLY -> {
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
                            send(summaryList)
                        }

                        GraphFilter.YEARLY -> {
                            val currentYear = LocalDate.now().year
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
                            send(summaryList)
                        }
                    }
                }
            }
        }
    }
}


