package com.finflio.core.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import com.finflio.ui.theme.GoldIcon


@Composable
fun Grapple(modifier: Modifier = Modifier, color: Color = GoldIcon) {
    Canvas(
        modifier = modifier,
        onDraw = {
            drawLine(
                color = color,
                start = Offset(size.width / 10, size.height / 2f),
                end = Offset(size.width - size.width / 10, size.height / 2f),
                strokeWidth = size.height / 3,
                cap = StrokeCap.Round,
            )
        }
    )
}