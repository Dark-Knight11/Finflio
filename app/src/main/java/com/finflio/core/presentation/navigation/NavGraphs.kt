package com.finflio.core.presentation.navigation

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph
@NavGraph
annotation class MainNavGraph(val start: Boolean = false)

@MainNavGraph(start = true)
@NavGraph
annotation class HomeNavGraph(val start: Boolean = false)

@MainNavGraph
@NavGraph
annotation class StatsNavGraph(val start: Boolean = false)

@RootNavGraph(start = true)
@NavGraph
annotation class AuthNavGraph(val start: Boolean = false)
