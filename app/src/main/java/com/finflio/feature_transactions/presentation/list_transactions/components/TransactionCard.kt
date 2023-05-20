package com.finflio.feature_transactions.presentation.list_transactions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflio.feature_transactions.presentation.add_edit_transactions.util.Categories
import com.finflio.feature_transactions.presentation.transaction_info.components.CategoryFab
import com.finflio.ui.theme.DMSans
import com.finflio.ui.theme.Expense
import com.finflio.ui.theme.GreenGradient
import com.finflio.ui.theme.TransactionCardBg
import com.finflio.ui.theme.TransactionDate
import com.finflio.ui.theme.TransferBg
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
    val formatter = if (type == "Unsettled") DateTimeFormatter.ofPattern("MMM dd, K:mm a")
    else DateTimeFormatter.ofPattern("K:mm a")
    val formattedDateTime = time.format(formatter)
    val titleField = when (type) {
        "Income" -> {
            if (from.isNullOrBlank()) category.category
            else from
        }

        "Expense" -> {
            if (to.isNullOrBlank()) category.category
            else to
        }

        else -> if (from.isNullOrBlank()) to else from
    }

    val amountField = when (type) {
        "Income" -> "+₹$amount".removeSuffix(".0")
        "Expense" -> "-₹$amount".removeSuffix(".0")
        else -> "₹$amount".removeSuffix(".0")
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
                when (type) {
                    "Income" -> Modifier.background(
                        brush = Brush.linearGradient(
                            0.80f to TransactionCardBg,
                            1f to GreenGradient[1]
                        )
                    )

                    "Expense" -> Modifier.background(
                        brush = Brush.linearGradient(
                            0.80f to TransactionCardBg,
                            1f to Expense
                        )
                    )

                    else -> Modifier.background(
                        brush = Brush.linearGradient(
                            0.6f to Color.Black,
                            0.7f to TransferBg
                        )
                    )
                }
            )
            .padding(15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            CategoryFab(icon = category.icon, colors = category.colors, size = 35.dp)
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    text = titleField ?: "",
                    fontSize = 13.sp,
                    color = Color.White.copy(0.87f),
                    fontFamily = DMSans,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
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
                text = amountField,
                fontSize = 12.sp,
                color = Color.White.copy(0.87f),
                fontFamily = DMSans,
                fontWeight = FontWeight.Normal
            )
        }
    }
}
