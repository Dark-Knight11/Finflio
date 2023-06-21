package com.finflio.feature_stats.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finflio.feature_stats.domain.model.SelectedChartFilters
import com.finflio.feature_stats.domain.model.StatsData
import com.finflio.feature_stats.domain.use_case.GetStatsUseCase
import com.finflio.feature_stats.presentation.util.GraphFilter
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.formatter.DecimalFormatAxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val getStatsUseCase: GetStatsUseCase
) : ViewModel() {

    private val statsData: MutableState<StatsData?> = mutableStateOf(null)
    private val _incomeChartEntry =
        mutableStateOf(ChartEntryModelProducer())
    val incomeChartEntry: State<ChartEntryModelProducer> = _incomeChartEntry

    private val _expenseChartEntry =
        mutableStateOf(ChartEntryModelProducer())
    val expenseChartEntry: State<ChartEntryModelProducer> = _expenseChartEntry

    private val _bottomAxisFormatter =
        mutableStateOf<AxisValueFormatter<AxisPosition.Horizontal.Bottom>>(
            DecimalFormatAxisValueFormatter()
        )
    val bottomAxisFormatter: State<AxisValueFormatter<AxisPosition.Horizontal.Bottom>> =
        _bottomAxisFormatter

    private val _selectedFilters = mutableStateOf<SelectedChartFilters>(SelectedChartFilters())
    val selectedFilters: State<SelectedChartFilters> = _selectedFilters

    init {
        refreshData()
    }

    fun changeFilter(filter: Enum<*>) {
        when (filter::class.simpleName) {
            "YMW" -> _selectedFilters.value = selectedFilters.value.copy(
                ymw = filter as GraphFilter.YMW
            )

            "Type" -> _selectedFilters.value = selectedFilters.value.copy(
                type = filter as GraphFilter.Type
            )
        }
        getData()
    }

    private fun getData() {
        when (selectedFilters.value.ymw) {
            GraphFilter.YMW.WEEKLY -> {
                _bottomAxisFormatter.value =
                    AxisValueFormatter { x, _ ->
                        listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")[x.toInt() % 7]
                    }
                val (totalIncome, totalExpense) = statsData.value?.weeklyData?.mapIndexed() { index, entry ->
                    Pair(
                        entryOf(index, entry.totalDailyIncome),
                        entryOf(index, entry.totalDailyExpense)
                    )
                }?.unzip() ?: Pair(emptyList(), emptyList())
                _incomeChartEntry.value.setEntries(totalIncome)
                _expenseChartEntry.value.setEntries(totalExpense)
            }

            GraphFilter.YMW.MONTHLY -> {
                _bottomAxisFormatter.value = DecimalFormatAxisValueFormatter()
                val (totalIncome, totalExpense) = statsData.value?.monthlyData?.mapIndexed() { index, entry ->
                    Pair(
                        entryOf(index + 1, entry.totalDailyIncome),
                        entryOf(index + 1, entry.totalDailyExpense)
                    )
                }?.unzip() ?: Pair(emptyList(), emptyList())
                _incomeChartEntry.value.setEntries(totalIncome)
                _expenseChartEntry.value.setEntries(totalExpense)
            }

            GraphFilter.YMW.YEARLY -> {
                _bottomAxisFormatter.value = AxisValueFormatter { x, _ ->
                    DateFormatSymbols.getInstance(Locale.getDefault()).shortMonths.toList()[x.toInt() % 12]
                }
                val (totalIncome, totalExpense) = statsData.value?.yearlyData?.mapIndexed() { index, entry ->
                    Pair(
                        entryOf(index, entry.totalDailyIncome),
                        entryOf(index, entry.totalDailyExpense)
                    )
                }?.unzip() ?: Pair(emptyList(), emptyList())
                _incomeChartEntry.value.setEntries(totalIncome)
                _expenseChartEntry.value.setEntries(totalExpense)
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            getStatsUseCase().collectLatest {
                statsData.value = it
            }
            getData()
        }
    }
}
