package com.finflio.feature_transactions.presentation.list_transactions.components

import com.finflio.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflio.ui.theme.DMSans
import com.finflio.ui.theme.GreenGradient
import com.finflio.ui.theme.TransactionCardBg
import com.finflio.ui.theme.TransactionDate

@Composable
fun TransactionCard(modifier: Modifier = Modifier, income: Boolean = false) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                shape = RoundedCornerShape(20.dp)
                clip = true
            }
            .then(
                if (income) Modifier.background(
                    brush = Brush.linearGradient(
                        0.80f to TransactionCardBg,
                        1f to GreenGradient[0],
                        1f to GreenGradient[1]
                    )
                )
                else Modifier.background(TransactionCardBg)
            )
            .padding(15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.shell),
                contentDescription = "Image",
                modifier = Modifier.size(35.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    text = "Shell",
                    fontSize = 13.sp,
                    color = Color.White.copy(0.87f),
                    fontFamily = DMSans,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Sep 02, 2022",
                    fontSize = 12.sp,
                    color = TransactionDate,
                    fontFamily = DMSans,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Box(
            modifier = Modifier
                .border(
                    shape = RoundedCornerShape(100),
                    color = Color.White.copy(0.20f),
                    width = 0.1.dp
                )
                .padding(vertical = 7.dp, horizontal = 20.dp)
        ) {
            Text(
                text = "-$45",
                fontSize = 12.sp,
                color = Color.White.copy(0.87f),
                fontFamily = DMSans,
                fontWeight = FontWeight.Normal
            )
        }
    }
}
