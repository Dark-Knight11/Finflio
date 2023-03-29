package com.finflio.feature_transactions.presentation.add_edit_transactions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.finflio.ui.theme.DMSans
import com.finflio.ui.theme.GoldIcon

@Composable
fun InputCard(
    title: String,
    content: @Composable () -> Unit
) {
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
            Spacer(modifier = Modifier.height(30.dp))
            content()
            Spacer(modifier = Modifier.height(5.dp))
            Divider(color = GoldIcon, thickness = 1.dp)
        }
    }
}
