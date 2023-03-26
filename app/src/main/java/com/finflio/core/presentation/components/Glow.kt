package com.finflio.core.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.skydoves.cloudy.Cloudy

@Composable
fun Glow(
    modifier: Modifier = Modifier,
    @androidx.annotation.IntRange(from = 0, to = 25) radius: Int = 16,
    trigger: Any? = null,
    secondTrigger: Any? = null,
    content: @Composable () -> Unit
) {
    Box(modifier.height(IntrinsicSize.Min).width(IntrinsicSize.Min)) {
        Cloudy(
            radius = radius,
            key1 = trigger,
            key2 = secondTrigger
        ) {
            Box(modifier = modifier.fillMaxSize())
        }
        content()
    }
}
