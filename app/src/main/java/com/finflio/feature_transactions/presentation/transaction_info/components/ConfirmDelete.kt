package com.finflio.feature_transactions.presentation.transaction_info.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.finflio.core.presentation.navigation.HomeNavGraph
import com.finflio.ui.theme.*
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
                Text(text = "No")
            }
            Button(
                onClick = {
                    resultNavigator.navigateBack(result = true)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (type == "Income") IncomeBG else ExpenseBG
                )
            ) {
                Text(text = "Yes")
            }
        }
    }
}