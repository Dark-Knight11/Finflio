package com.finflio.feature_transactions.presentation.transaction_info.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.finflio.core.presentation.navigation.HomeNavGraph
import com.finflio.ui.theme.DMSans
import com.finflio.ui.theme.ExpenseBG
import com.finflio.ui.theme.IncomeBG
import com.finflio.ui.theme.MainBackground
import com.finflio.ui.theme.TransferBg
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle

@HomeNavGraph
@Destination(style = DestinationStyle.Dialog::class)
@Composable
fun DeleteConfirmation(
    resultNavigator: ResultBackNavigator<Boolean>,
    type: String?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                shape = RoundedCornerShape(10.dp)
                clip = true
            }
            .background(MainBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text(
            text = "Are you sure you want to delete this Transaction?",
            fontFamily = DMSans,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    resultNavigator.navigateBack(result = false)
                },
                border = BorderStroke(0.1.dp, Color.White.copy(0.5f)),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MainBackground
                )
            ) {
                Text(text = "No", color = Color.White)
            }
            Button(
                onClick = {
                    resultNavigator.navigateBack(result = true)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = when (type) {
                        "Expense" -> ExpenseBG
                        "Income" -> IncomeBG
                        else -> TransferBg
                    }
                )
            ) {
                Text(text = "Yes", color = Color.White)
            }
        }
    }
}
