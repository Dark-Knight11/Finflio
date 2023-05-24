package com.finflio.feature_transactions.presentation.add_edit_transactions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.finflio.ui.theme.DMSans

@Composable
fun InputCard(
    title: String,
    content: @Composable () -> Unit
) {
    val customTextSelectionColors = TextSelectionColors(
        handleColor = Transparent,
        backgroundColor = Transparent
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                shape = RoundedCornerShape(20.dp)
                clip = true
            }
            .background(Color.White.copy(0.1f))
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(
                text = title,
                color = Color.White,
                fontFamily = DMSans,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(15.dp))
            CompositionLocalProvider(
                LocalTextSelectionColors provides customTextSelectionColors
            ) { content() }
        }
    }
}
