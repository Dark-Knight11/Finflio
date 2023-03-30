package com.finflio.feature_transactions.presentation.transaction_info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflio.ui.theme.GreenGradient
import com.finflio.ui.theme.RedGradient

@Composable
fun EditButton(modifier: Modifier, type: String) {
    Box(
        modifier
            .graphicsLayer {
                shape = RoundedCornerShape(15.dp)
                clip = true
            }
            .fillMaxWidth()
            .then(
                if (type == "Expense")
                    Modifier.background(brush = Brush.linearGradient(RedGradient))
                else
                    Modifier.background(brush = Brush.linearGradient(GreenGradient))
            )
            .padding(vertical = 15.dp)
    ) {
        Text(
            text = "Edit",
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
    }
}