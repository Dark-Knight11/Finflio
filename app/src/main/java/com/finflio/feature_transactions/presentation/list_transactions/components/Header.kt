package com.finflio.feature_transactions.presentation.list_transactions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
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
    month: String,
    onSelect: (String) -> Unit,
    onClick: () -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = 45.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            MonthDropDown(Modifier.fillMaxWidth(), month) { onSelect(it) }
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                textAlign = TextAlign.Center
            )
        }

        IconButton(
            onClick = { onClick() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "logout",
                tint = Color.White
            )
        }
    }
}
