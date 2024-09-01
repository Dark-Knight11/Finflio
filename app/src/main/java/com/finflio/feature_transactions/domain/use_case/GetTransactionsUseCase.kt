package com.finflio.feature_transactions.domain.use_case

import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.insertSeparators
import androidx.paging.map
import com.finflio.feature_transactions.domain.mapper.toTransaction
import com.finflio.feature_transactions.domain.model.TransactionModel
import com.finflio.feature_transactions.domain.repository.TransactionsRepository
import com.finflio.feature_transactions.domain.util.getDayOfMonthSuffix
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTransactionsUseCase @Inject constructor(
    private val transactionRepo: TransactionsRepository
) {
    operator fun invoke(month: Month, year: Year): Flow<PagingData<TransactionModel>> {
        val calendar = Calendar.getInstance()
        val currentDate = LocalDate.now()
        return transactionRepo.getTransactions(month.name, year.value).map { pagingData ->
            pagingData.map {
                TransactionModel.TransactionItem(it.first.toTransaction())
            }
        }.map { pagingData ->
            pagingData.filter { it.transaction.type != "Unsettled" }
        }.map {
            it.insertSeparators { before, after ->
                // TODO If network is down then first data grp doesn't have any separator
                if (after == null) return@insertSeparators null
                val dateOfAfterItem = after.transaction.timestamp.toLocalDate()
                if (before == null) {
                    val dateFormat = SimpleDateFormat(
                        /* pattern = */ "EEEE - d'${
                        getDayOfMonthSuffix(
                            dateOfAfterItem.dayOfMonth
                        )
                        }' MMM",
                        /* locale = */ Locale.getDefault()
                    )
                    calendar.set(Calendar.MONTH, dateOfAfterItem.monthValue - 1)
                    calendar.set(Calendar.DAY_OF_MONTH, dateOfAfterItem.dayOfMonth)
                    return@insertSeparators TransactionModel.SeparatorItem(
                        when (dateOfAfterItem) {
                            currentDate -> "Today"
                            currentDate.minusDays(1) -> "Yesterday"
                            else -> dateFormat.format(calendar.time)
                        }
                    )
                }
                val dateOfBeforeItem = before.transaction.timestamp.toLocalDate()
                if (dateOfAfterItem != dateOfBeforeItem) {
                    val dateFormat = SimpleDateFormat(
                        /* pattern = */ "EEEE - d'${
                        getDayOfMonthSuffix(dateOfAfterItem.dayOfMonth)
                        }' MMM",
                        /* locale = */ Locale.getDefault()
                    )
                    calendar.set(Calendar.MONTH, dateOfAfterItem.monthValue - 1)
                    calendar.set(Calendar.DAY_OF_MONTH, dateOfAfterItem.dayOfMonth)
                    return@insertSeparators TransactionModel.SeparatorItem(
                        when (dateOfAfterItem) {
                            currentDate -> "Today"
                            currentDate.minusDays(1) -> "Yesterday"
                            else -> dateFormat.format(calendar.time)
                        }
                    )
                } else {
                    null
                }
            }
        }
    }
}