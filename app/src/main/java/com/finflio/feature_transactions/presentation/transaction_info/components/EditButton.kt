package com.finflio.feature_transactions.presentation.transaction_info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflio.ui.theme.GreenGradient
import com.finflio.ui.theme.RedGradient
import com.finflio.ui.theme.TransferGradient

@Composable
fun EditButton(
    modifier: Modifier,
    type: String,
    onClick: () -> Unit
) {
    Box(
        modifier
            .graphicsLayer {
                shape = RoundedCornerShape(15.dp)
                clip = true
            }
            .clickable(remember { MutableInteractionSource() }, null) {
                onClick()
            }
            .fillMaxWidth()
            .background(
                brush = Brush
                    .linearGradient(
                        when (type) {
                            "Expense" -> RedGradient
                            "Income" -> GreenGradient
                            else -> TransferGradient
                        }
                    )
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