package com.finflio.feature_stats.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.finflio.R
import com.finflio.feature_stats.presentation.util.GraphFilter
import com.finflio.ui.theme.Gold

@Composable
fun ChartStyleFilter(
    modifier: Modifier = Modifier,
    currentlySelectedGraphFilter: GraphFilter.ChartStyle?,
    onClick: (GraphFilter.ChartStyle) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        listOf(GraphFilter.ChartStyle.LINECHART, GraphFilter.ChartStyle.BARCHART).forEach {
            IconFilter(
                graphFilter = it,
                modifier = Modifier
                    .clickable(remember { MutableInteractionSource() }, null) {
                        onClick(it)
                    }
                    .then(
                        if (currentlySelectedGraphFilter == it) {
                            Modifier.background(
                                Brush.linearGradient(
                                    0.0f to Gold.copy(0.6f),
                                    1.0f to Gold.copy(0.2f)
                                )
                            )
                        } else {
                            Modifier
                        }
                    )
            )
        }
    }
}

@Composable
fun IconFilter(
    modifier: Modifier = Modifier,
    graphFilter: GraphFilter.ChartStyle
) {
    Box(
        modifier = Modifier
            .graphicsLayer {
                shape = RoundedCornerShape(8.dp)
                clip = true
            }
            .background(Color.Black)
            .then(modifier)
            .padding(vertical = 6.dp, horizontal = 10.dp)
    ) {
        Icon(
            painter = painterResource(
                id = if (graphFilter == GraphFilter.ChartStyle.BARCHART) R.drawable.ic_bar else R.drawable.ic_line
            ),
            contentDescription = "chart style",
            tint = Color.White,
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.Center)
        )
    }
}
