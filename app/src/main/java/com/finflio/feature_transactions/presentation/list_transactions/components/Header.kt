package com.finflio.feature_transactions.presentation.list_transactions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflio.ui.theme.Inter
import com.finflio.ui.theme.SecondaryText

@Composable
fun Header(
    total: Float,
    onSelect: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 50.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        MonthDropDown(Modifier.fillMaxWidth(0.8f)) { onSelect(it) }
        Text(
            text = "This month you spent",
            fontFamily = Inter,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = SecondaryText
        )
        Text(
            text = "₹" + total.toString().removeSuffix(".0"),
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Inter,
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            textAlign = TextAlign.Center
        )
    }
}

