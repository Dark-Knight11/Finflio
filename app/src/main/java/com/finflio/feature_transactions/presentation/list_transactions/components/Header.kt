package com.finflio.feature_transactions.presentation.list_transactions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflio.core.presentation.components.Glow
import com.finflio.ui.theme.Inter
import com.finflio.ui.theme.SecondaryText

@Composable
fun Header(trigger: Boolean) {
    Column(
        modifier = Modifier
            .padding(top = 50.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        MonthDropDown(modifier = Modifier.fillMaxWidth())
        Text(
            text = "This month you spent",
            fontFamily = Inter,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = SecondaryText
        )
        val text by remember { mutableStateOf("₹9400") }
        Glow(
            modifier = Modifier.padding(bottom = 20.dp),
            radius = 16,
            trigger = text,
            secondTrigger = trigger
        ) {
            Text(
                text = text,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Inter,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

