package com.finflio.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import com.finflio.ui.theme.DiamondShape
import com.finflio.ui.theme.RedGradient

@Composable
fun DiamondFab(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    size: Dp,
    icon: @Composable () -> Unit
) {
    FabWithoutIndication(
        onClick = onClick,
        content = icon,
        modifier = modifier
            .size(size)
            .graphicsLayer {
                shape = DiamondShape(size.toPx())
                clip = true
            }
            .background(
                brush = Brush.verticalGradient(RedGradient)
            )
    )
}
