package com.finflio.feature_stats.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ChipDefaults
import androidx.compose.material.FilterChip
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.finflio.core.presentation.navigation.StatsNavGraph
import com.finflio.feature_stats.presentation.util.GraphFilter
import com.finflio.feature_stats.presentation.util.rememberChartStyle
import com.finflio.feature_stats.presentation.util.rememberMarker
import com.finflio.ui.theme.Expense
import com.finflio.ui.theme.GoldIcon
import com.finflio.ui.theme.Income
import com.finflio.ui.theme.navigationBarHeight
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shape.roundedCornerShape
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.axis.formatter.DecimalFormatAxisValueFormatter
import com.patrykandpatrick.vico.core.chart.DefaultPointConnector
import com.patrykandpatrick.vico.core.chart.composed.plus
import com.patrykandpatrick.vico.core.chart.copy
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.entry.composed.plus
import com.ramcosta.composedestinations.annotation.Destination
import java.math.RoundingMode

@StatsNavGraph(start = true)
@Destination
@Composable
fun ShowStats(viewModel: StatsViewModel = hiltViewModel()) {

    val expenseChartEntry = viewModel.expenseChartEntry.value
    val incomeChartEntry = viewModel.incomeChartEntry.value
    val bottomAxisFormatter = viewModel.bottomAxisFormatter.value

    val marker = rememberMarker()
    var currentlySelectedFilter by remember() {
        mutableStateOf(GraphFilter.WEEKLY)
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = navigationBarHeight)
        ) {
            GraphFilter.values().forEach {
                FilterChip(
                    selected = it == currentlySelectedFilter,
                    onClick = {
                        currentlySelectedFilter = it
                        viewModel.generateEntry(it)
                    },
                    colors = ChipDefaults.filterChipColors(
                        selectedBackgroundColor = GoldIcon.copy(0.5f),
                        backgroundColor = Color.Black
                    )
                ) {
                    Text(
                        text = it.name.lowercase().replaceFirstChar { char ->
                            char.titlecase()
                        }
                    )
                }
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(size = 20.dp),
                )
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            ProvideChartStyle(rememberChartStyle(listOf(Income), listOf(Expense))) {
                val defaultColumns = currentChartStyle.columnChart.columns
                val defaultLines = currentChartStyle.lineChart.lines
                val columnChart = columnChart(
                    remember(defaultColumns) {
                        defaultColumns.map { defaultColumn ->
                            LineComponent(
                                defaultColumn.color,
                                defaultColumn.thicknessDp,
                                Shapes.roundedCornerShape(topLeft = 5.dp, topRight = 5.dp),
                            )
                        }
                    },
                )
                val lineChart = lineChart(
                    remember(defaultLines) {
                        defaultLines.map { defaultLine ->
                            defaultLine.copy(
                                pointConnector = DefaultPointConnector(cubicStrength = 0f)
                            )
                        }
                    },
                    axisValuesOverrider = AxisValuesOverrider.adaptiveYValues(
                        1.2f,
                        round = true,
                    ),
//                    spacing = 30.dp
//                        persistentMarkers = mapOf(-1f to marker)
                )
                Chart(
                    chart = remember(columnChart, lineChart) { columnChart + lineChart },
                    chartModelProducer = incomeChartEntry + expenseChartEntry,
                    startAxis = startAxis(
                        valueFormatter = DecimalFormatAxisValueFormatter(
                            pattern = "#",
                            roundingMode = RoundingMode.CEILING
                        )
                    ),
                    bottomAxis = bottomAxis(valueFormatter = bottomAxisFormatter),
                    marker = marker
                )
            }
        }
    }
}