package com.finflio.ui.theme

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

val navigationBarHeight: Dp
    @Composable
    get() = with(LocalDensity.current) {
        WindowInsets.navigationBars.getBottom(this).toDp()
    }