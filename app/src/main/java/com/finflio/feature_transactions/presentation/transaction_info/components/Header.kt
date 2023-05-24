package com.finflio.feature_transactions.presentation.transaction_info.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflio.core.presentation.components.CategoryFab
import com.finflio.feature_transactions.presentation.add_edit_transactions.util.Categories
import com.finflio.ui.theme.Inter
import com.finflio.ui.theme.OffWhite
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun Header(
    amount: Float,
    timestamp: LocalDateTime,
    type: String,
    category: Categories,
    paymentMethod: String,
    infoBarModifier: Modifier
) {
    val formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy K:mm a")
    val formattedDateTime = timestamp.format(formatter)
    // "Saturday 4 June 2021 16:20"
    Text(
        text = "₹" + amount.toString().removeSuffix(".0"),
        color = Color.White,
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = formattedDateTime,
        color = OffWhite,
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(30.dp))
    InfoBar(
        modifier = infoBarModifier,
        type = type,
        category = category.category,
        paymentMethod = paymentMethod
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        DashedDivider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White.copy(0.5f),
            thickness = 1.dp,
            intervals = floatArrayOf(30f, 30f),
            phase = 20f
        )
        CategoryFab(
            icon = category.icon,
            colors = category.colors,
            size = 50.dp,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}
