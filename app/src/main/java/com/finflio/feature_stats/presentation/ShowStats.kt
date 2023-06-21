package com.finflio.feature_stats.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.finflio.core.presentation.components.CommonSnackBar
import com.finflio.core.presentation.navigation.StatsNavGraph
import com.finflio.feature_stats.presentation.components.ChartBox
import com.finflio.feature_stats.presentation.components.ChartStyleFilter
import com.finflio.feature_stats.presentation.components.RefreshButton
import com.finflio.feature_stats.presentation.components.TypeFilter
import com.finflio.feature_stats.presentation.components.YMWFilter
import com.finflio.feature_stats.presentation.util.GraphFilter
import com.finflio.feature_stats.presentation.util.StatsUiEvent
import com.finflio.ui.theme.ExpenseBG
import com.finflio.ui.theme.navigationBottomBarHeight
import com.finflio.ui.theme.navigationTopBarHeight
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.flow.collectLatest

@StatsNavGraph(start = true)
@Destination
@Composable
fun ShowStats(viewModel: StatsViewModel = hiltViewModel()) {
    val expenseChartEntry = viewModel.expenseChartEntry.value
    val incomeChartEntry = viewModel.incomeChartEntry.value
    val bottomAxisFormatter = viewModel.bottomAxisFormatter.value

    val selectedFilter = viewModel.selectedFilters.value
    var currentlySelectedGraphFilter by rememberSaveable {
        mutableStateOf<GraphFilter.ChartStyle?>(null)
    }
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(true) {
        viewModel.eventFlow.collectLatest {
            when (it) {
                is StatsUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(it.message)
                }
            }
        }
    }

    CommonSnackBar(
        snackBarHostState = snackbarHostState,
        modifier = Modifier.padding(bottom = navigationBottomBarHeight + 95.dp),
        backgroundColor = ExpenseBG.copy(0.4f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = navigationTopBarHeight),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TypeFilter(currentlySelectedFilter = selectedFilter.type) {
                viewModel.changeFilter(it)
                currentlySelectedGraphFilter = if (it == GraphFilter.Type.COMBINED) {
                    null
                } else {
                    GraphFilter.ChartStyle.LINECHART
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ChartStyleFilter(currentlySelectedGraphFilter = currentlySelectedGraphFilter) {
                    if (selectedFilter.type != GraphFilter.Type.COMBINED) {
                        currentlySelectedGraphFilter = it
                    }
                }
                RefreshButton() {
                    viewModel.refreshData()
                }
//            CustomRangeFilter() {
//            }
            }
            ChartBox(
                typeFilter = selectedFilter.type,
                chartStyle = currentlySelectedGraphFilter,
                expenseChartEntry = expenseChartEntry,
                incomeChartEntry = incomeChartEntry,
                bottomAxisFormatter = bottomAxisFormatter
            )
            YMWFilter() {
                viewModel.changeFilter(it)
            }
        }
    }
}
