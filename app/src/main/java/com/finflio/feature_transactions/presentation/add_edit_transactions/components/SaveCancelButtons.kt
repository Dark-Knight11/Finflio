package com.finflio.feature_transactions.presentation.add_edit_transactions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflio.ui.theme.RedGradient

@Composable
fun SaveCancelButtons(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(top = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .graphicsLayer {
                    shape = RoundedCornerShape(15.dp)
                    clip = true
                }
                .background(Color.White.copy(0.08f))
                .width(127.dp)
                .padding(vertical = 15.dp)
        ) {
            Text(
                text = "Delete",
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.width(36.dp))
        Box(
            Modifier
                .graphicsLayer {
                    shape = RoundedCornerShape(15.dp)
                    clip = true
                }
                .background(brush = Brush.linearGradient(RedGradient))
                .width(127.dp)
                .padding(vertical = 15.dp)
        ) {
            Text(
                text = "Save",
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
        }
    }
}