package com.finflio.feature_transactions.presentation.add_edit_transactions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.finflio.ui.theme.DMSans
import com.finflio.ui.theme.GreenGradient
import com.finflio.ui.theme.RedGradient

@Composable
fun AddEditTransactionTopAppBar(
    modifier: Modifier = Modifier,
    type: String,
    onBackPressed: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(top = 70.dp)
            .background(
                brush = Brush.linearGradient(if (type == "Expense") RedGradient else GreenGradient)
            )
            .padding(vertical = 13.dp)
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "back",
            tint = Color.White,
            modifier = Modifier
                .padding(start = 10.dp)
                .clickable(remember { MutableInteractionSource() }, null) {
                    onBackPressed()
                }
        )
        Text(
            text =  type,
            fontFamily = DMSans,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}