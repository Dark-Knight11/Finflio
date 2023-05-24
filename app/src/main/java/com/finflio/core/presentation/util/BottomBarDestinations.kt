package com.finflio.core.presentation.util

import com.finflio.NavGraph
import com.finflio.NavGraphs
import com.finflio.R

enum class BottomBarDestinations(
    val direction: NavGraph,
    val icon: Int,
    val label: String
) {
    Home(NavGraphs.home, R.drawable.ic_home, "Home"),
    Stats(NavGraphs.stats, R.drawable.ic_stats, "Stats")
}
