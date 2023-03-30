package com.finflio.feature_transactions.presentation.transaction_info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CategoryFab(
    icon: Int,
    modifier: Modifier = Modifier,
    size: Dp = 50.dp,
    colors: List<Color> = listOf(Color(0xFF61D8D8), Color(0xFF3972D4))
) {
    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                shape = CircleShape
                clip = true
            }
            .background(
                brush = Brush.verticalGradient(
                    0.0f to colors[0],
                    1.0f to colors[1]
                )
            )
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "category",
            tint = Color.Black,
            modifier = Modifier.size(size)
        )
    }
}