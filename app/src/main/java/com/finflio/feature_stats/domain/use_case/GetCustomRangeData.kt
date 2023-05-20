package com.finflio.feature_stats.domain.use_case

import com.finflio.core.domain.repository.TransactionsRepository
import com.finflio.feature_stats.domain.model.SelectedChartFilters
import com.finflio.feature_stats.domain.model.Summary
import com.finflio.feature_stats.domain.util.calculateMonthlyData
import com.finflio.feature_stats.domain.util.calculateWeeklyData
import com.finflio.feature_stats.domain.util.calculateYearlyData
import com.finflio.feature_stats.presentation.util.GraphFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class GetCustomRangeData @Inject constructor(
    private val repo: TransactionsRepository
) {
    suspend operator fun invoke(filter: SelectedChartFilters): Flow<List<Summary>> {
        return channelFlow {
            launch(Dispatchers.IO) {
                repo.getTransactions().collectLatest { transactionList ->
                    val currentDate = LocalDate.now()
                    when (filter.ymw) {
                        GraphFilter.YMW.WEEKLY -> {
                            val summaryList =
                                calculateWeeklyData(currentDate, transactionList, filter.type)
                            send(summaryList)
                        }

                        GraphFilter.YMW.MONTHLY -> {
                            val summaryList =
                                calculateMonthlyData(currentDate, transactionList, filter.type)
                            send(summaryList)
                        }

                        GraphFilter.YMW.YEARLY -> {
                            val summaryList = calculateYearlyData(transactionList, filter.type)
                            send(summaryList)
                        }
                    }
                }
            }
        }
    }
}


