package com.finflio.feature_transactions.presentation.transaction_info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflio.ui.theme.BottomNavBlue
import com.finflio.ui.theme.DMSans

@Composable
fun InfoBar(
    modifier: Modifier = Modifier,
    type: String,
    category: String,
    paymentMethod: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(25.dp)
            .graphicsLayer {
                shape = RoundedCornerShape(50.dp)
                clip = true
            }
            .background(BottomNavBlue)
            .padding(vertical = 16.dp, horizontal = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Type",
                fontFamily = DMSans,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                color = Color.White
            )
            Text(
                text = type,
                fontFamily = DMSans,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color.White
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Category",
                fontFamily = DMSans,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                color = Color.White
            )
            Text(
                text = category,
                fontFamily = DMSans,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color.White
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Payment",
                fontFamily = DMSans,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                color = Color.White
            )
            Text(
                text = paymentMethod,
                fontFamily = DMSans,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color.White
            )
        }
    }
}
