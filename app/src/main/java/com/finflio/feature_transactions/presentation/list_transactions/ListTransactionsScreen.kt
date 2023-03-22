package com.finflio.feature_transactions.presentation.list_transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.finflio.core.presentation.navigation.HomeNavGraph
import com.ramcosta.composedestinations.annotation.Destination

@HomeNavGraph(start = true)
@Destination
@Composable
fun ListTransactions() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Transactions",
            color = Color.White,
            fontSize = 30.sp
        )
    }
}