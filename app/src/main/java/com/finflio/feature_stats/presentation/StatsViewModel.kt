package com.finflio.feature_stats.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finflio.feature_stats.domain.model.SelectedChartFilters
import com.finflio.feature_stats.domain.use_case.GetCustomRangeData
import com.finflio.feature_stats.presentation.util.GraphFilter
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.formatter.DecimalFormatAxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.DateFormatSymbols
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val getCustomRangeData: GetCustomRangeData
) : ViewModel() {

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
        generateEntry()
    }

    private fun generateEntry() {
        viewModelScope.launch {
            val axisFormatter = when (selectedFilters.value.ymw) {
                GraphFilter.YMW.WEEKLY -> AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ ->
                    listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")[x.toInt() % 7]
                }

                GraphFilter.YMW.MONTHLY -> DecimalFormatAxisValueFormatter()

                GraphFilter.YMW.YEARLY -> AxisValueFormatter { x, _ ->
                    DateFormatSymbols.getInstance(Locale.getDefault()).shortMonths.toList()[x.toInt() % 12]
                }
            }

            getCustomRangeData(selectedFilters.value).collectLatest { summary ->
                val (totalIncome, totalExpense) = summary.mapIndexed { index, entry ->
                    var ind = index
                    if (selectedFilters.value.ymw == GraphFilter.YMW.MONTHLY) ind += 1
                    Pair(entryOf(ind, entry.income), entryOf(ind, entry.expenses))
                }.unzip()

                _incomeChartEntry.value.setEntries(totalIncome)
                _expenseChartEntry.value.setEntries(totalExpense)
                _bottomAxisFormatter.value = axisFormatter
            }
        }
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
        generateEntry()
    }
}
