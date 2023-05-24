package com.finflio.feature_transactions.domain.use_case

import com.finflio.core.domain.repository.TransactionsRepository
import com.finflio.feature_transactions.domain.model.TransactionDisplay
import com.finflio.feature_transactions.domain.util.getDayOfMonthSuffix
import com.patrykandpatrick.vico.core.extension.sumByFloat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Month
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GetTransactionsUseCase @Inject constructor(
    private val repo: TransactionsRepository
) {
    operator fun invoke(month: Month): Flow<Pair<Float, List<TransactionDisplay>>> {
        val calendar = Calendar.getInstance()
        val currentDate = LocalDate.now()
        return channelFlow {
            launch(Dispatchers.IO) {
                repo.getTransactions().collectLatest { transactionList ->
                    val (total, list) = transactionList.asSequence().filter {
                        (it.type == "Expense" || it.type == "Income") && (it.timestamp.month == month && it.timestamp.year == currentDate.year)
                    }
                        .sortedByDescending { it.timestamp.dayOfMonth }
                        .groupBy { it.timestamp.dayOfMonth }.map { (dayOfMonth, transactions) ->
                            val dateFormat = SimpleDateFormat(
                                /* pattern = */ "EEEE - d'${getDayOfMonthSuffix(dayOfMonth)}' MMM",
                                /* locale = */ Locale.getDefault()
                            )
                            calendar.set(Calendar.MONTH, transactions[0].timestamp.monthValue - 1)
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            val day = when (dayOfMonth) {
                                currentDate.dayOfMonth -> if (currentDate.month == transactions[0].timestamp.month) {
                                    "Today"
                                } else {
                                    dateFormat.format(calendar.time)
                                }

                                currentDate.minusDays(1).dayOfMonth -> if (currentDate.month == transactions[0].timestamp.month) {
                                    "Yesterday"
                                } else {
                                    dateFormat.format(calendar.time)
                                }

                                else -> dateFormat.format(calendar.time)
                            }
                            transactions.filter { it.type == "Expense" }.sumByFloat { it.amount } to
                                TransactionDisplay(
                                    day = day,
                                    transactions = transactions.sortedBy { it.timestamp }
                                )
                        }.unzip()

                    send(total.sum() to list)
                }
            }
        }
    }
}
