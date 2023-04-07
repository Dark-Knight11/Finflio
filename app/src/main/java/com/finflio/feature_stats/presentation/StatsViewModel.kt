package com.finflio.feature_stats.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finflio.feature_stats.domain.use_case.GetCustomRangeData
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
import java.util.*
import javax.inject.Inject

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


    init {
        generateEntry(GraphFilter.WEEKLY)
    }

    fun generateEntry(filter: GraphFilter) {
        viewModelScope.launch {
            val axisFormatter = when (filter) {
                GraphFilter.WEEKLY -> AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ ->
                    listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")[x.toInt() % 7]
                }
                GraphFilter.MONTHLY -> DecimalFormatAxisValueFormatter()
                GraphFilter.YEARLY -> AxisValueFormatter { x, _ ->
                    DateFormatSymbols.getInstance(Locale.getDefault()).shortMonths.toList()[x.toInt() % 12]
                }
            }

            getCustomRangeData(filter).collectLatest { summary ->
                val (totalIncome, totalExpense) = summary.mapIndexed { index, entry ->
                    var ind = index
                    if (filter == GraphFilter.MONTHLY) ind += 1
                    Pair(entryOf(ind, entry.income), entryOf(ind, entry.expenses))
                }.unzip()

                _incomeChartEntry.value.setEntries(totalIncome)
                _expenseChartEntry.value.setEntries(totalExpense)
                _bottomAxisFormatter.value = axisFormatter
            }
        }
    }
}
