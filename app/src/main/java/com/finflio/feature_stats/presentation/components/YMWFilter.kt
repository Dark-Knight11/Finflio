package com.finflio.feature_stats.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflio.feature_stats.presentation.util.GraphFilter
import com.finflio.ui.theme.GoldGradient
import com.finflio.ui.theme.Poppins

@Composable
fun YMWFilter(
    modifier: Modifier = Modifier,
    onClick: (GraphFilter.YMW) -> Unit
) {
    var currentlySelectedFilter by remember() {
        mutableStateOf(GraphFilter.YMW.WEEKLY)
    }
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top,
        modifier = modifier.fillMaxWidth()
    ) {
        GraphFilter.YMW.values().forEach {
            YMWBox(
                text = it.name.lowercase().replaceFirstChar { char ->
                    char.titlecase()
                },
                modifier = Modifier
                    .graphicsLayer {
                        shape = RoundedCornerShape(8.dp)
                        clip = true
                    }
                    .background(Color.Black)
                    .then(
                        if (currentlySelectedFilter == it) Modifier.background(
                            brush = Brush.linearGradient(
                                GoldGradient
                            )
                        )
                        else Modifier
                    )
                    .clickable(remember { MutableInteractionSource() }, null) {
                        currentlySelectedFilter = it
                        onClick(it)
                    }

            )
        }
    }
}


@Composable
fun YMWBox(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color.Black.copy(0.50f))
            .padding(vertical = 8.dp, horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontFamily = Poppins,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}