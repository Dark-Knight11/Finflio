package com.finflio.feature_stats.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.finflio.feature_stats.presentation.util.GraphFilter
import com.finflio.feature_stats.presentation.util.rememberChartStyle
import com.finflio.feature_stats.presentation.util.rememberMarker
import com.finflio.ui.theme.Expense
import com.finflio.ui.theme.Income
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shape.roundedCornerShape
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.formatter.DecimalFormatAxisValueFormatter
import com.patrykandpatrick.vico.core.chart.DefaultPointConnector
import com.patrykandpatrick.vico.core.chart.composed.plus
import com.patrykandpatrick.vico.core.chart.copy
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.plus
import java.math.RoundingMode

@Composable
fun ChartBox(
    typeFilter: GraphFilter.Type,
    chartStyle: GraphFilter.ChartStyle?,
    expenseChartEntry: ChartEntryModelProducer,
    incomeChartEntry: ChartEntryModelProducer,
    bottomAxisFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom>
) {
    val marker = rememberMarker()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(
                color = Color.Black,
                shape = RoundedCornerShape(size = 20.dp),
            )
            .padding(16.dp)
            .fillMaxWidth(),
    ) {
        if (chartStyle == null) {
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
//                    spacing = 10.dp
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
        } else {
            when (chartStyle) {
                GraphFilter.ChartStyle.LINECHART -> {
                    ProvideChartStyle(
                        rememberChartStyle(
                            if (typeFilter == GraphFilter.Type.EXPENSE) listOf(Expense)
                            else listOf(Income)
                        )
                    ) {
                        val lineChart = lineChart(
                            axisValuesOverrider = AxisValuesOverrider.adaptiveYValues(
                                1.2f,
                                round = true,
                            ),
                            spacing = 20.dp
                            // persistentMarkers = mapOf(-1f to marker)
                        )
                        Chart(
                            chart = lineChart,
                            chartModelProducer = if (typeFilter == GraphFilter.Type.EXPENSE) expenseChartEntry else incomeChartEntry,
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

                GraphFilter.ChartStyle.BARCHART -> {
                    ProvideChartStyle(
                        rememberChartStyle(
                            if (typeFilter == GraphFilter.Type.EXPENSE) listOf(Expense)
                            else listOf(Income)
                        )
                    ) {
                        val defaultColumns = currentChartStyle.columnChart.columns
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
                        Chart(
                            chart = columnChart,
                            chartModelProducer = if (typeFilter == GraphFilter.Type.EXPENSE) expenseChartEntry else incomeChartEntry,
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
    }
}
