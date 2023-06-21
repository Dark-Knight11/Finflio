package com.finflio.feature_stats.presentation.util

sealed class StatsUiEvent {
    data class ShowSnackbar(val message: String) : StatsUiEvent()
}
