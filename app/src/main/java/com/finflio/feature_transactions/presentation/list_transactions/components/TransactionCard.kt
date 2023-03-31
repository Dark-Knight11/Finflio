package com.finflio.feature_transactions.presentation.list_transactions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflio.feature_transactions.presentation.add_edit_transactions.util.Categories
import com.finflio.feature_transactions.presentation.transaction_info.components.CategoryFab
import com.finflio.ui.theme.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TransactionCard(
    modifier: Modifier = Modifier,
    category: Categories,
    to: String? = null,
    from: String? = null,
    time: LocalDateTime,
    amount: Float,
    type: String,
    onClick: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    val formattedDateTime = time.format(formatter)
    val display = if (type == "Income") {
        if (from.isNullOrBlank()) category.category
        else from
    } else {
        if (to.isNullOrBlank()) category.category
        else to
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(remember { MutableInteractionSource() }, null) { onClick() }
            .graphicsLayer {
                shape = RoundedCornerShape(20.dp)
                clip = true
            }
            .background(Color.Black)
            .then(
                if (type == "Income") Modifier.background(
                    brush = Brush.linearGradient(
                        0.80f to TransactionCardBg,
                        1f to GreenGradient[1]
                    )
                )
                else Modifier.background(
                    brush = Brush.linearGradient(
                        0.80f to TransactionCardBg,
                        1f to Expense
                    )
                )
            )
            .padding(15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            CategoryFab(icon = category.icon, colors = category.colors, size = 35.dp)
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    text = display,
                    fontSize = 13.sp,
                    color = Color.White.copy(0.87f),
                    fontFamily = DMSans,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formattedDateTime,
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
                text = if (type == "Income") "+₹$amount".removeSuffix(".0")
                else "-₹$amount".removeSuffix(".0"),
                fontSize = 12.sp,
                color = Color.White.copy(0.87f),
                fontFamily = DMSans,
                fontWeight = FontWeight.Normal
            )
        }
    }
}
