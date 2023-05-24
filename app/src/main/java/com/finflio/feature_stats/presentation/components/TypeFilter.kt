package com.finflio.feature_stats.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflio.feature_stats.presentation.util.GraphFilter
import com.finflio.ui.theme.GoldGradient
import com.finflio.ui.theme.Poppins

@Composable
fun TypeFilter(
    modifier: Modifier = Modifier,
    currentlySelectedFilter: GraphFilter.Type,
    onClick: (GraphFilter.Type) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 13.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GraphFilter.Type.values().forEach {
            FilterBox(
                text = it.name.lowercase().replaceFirstChar { char ->
                    char.titlecase()
                },
                Modifier
                    .weight(1f)
                    .clickable(remember { MutableInteractionSource() }, null) {
                        onClick(it)
                    }
                    .then(
                        if (currentlySelectedFilter == it) {
                            Modifier.background(
                                brush = Brush.linearGradient(GoldGradient)
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
fun FilterBox(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color.Black.copy(0.50f))
            .padding(vertical = 13.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontFamily = Poppins,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
