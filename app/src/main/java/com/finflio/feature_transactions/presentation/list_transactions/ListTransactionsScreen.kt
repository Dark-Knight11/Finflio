package com.finflio.feature_transactions.presentation.list_transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.finflio.core.presentation.navigation.HomeNavGraph
import com.finflio.feature_transactions.presentation.list_transactions.components.Header
import com.finflio.feature_transactions.presentation.list_transactions.components.TransactionCard
import com.finflio.ui.theme.DMSans
import com.finflio.ui.theme.TransactionsLazyCol
import com.ramcosta.composedestinations.annotation.Destination

@HomeNavGraph(start = true)
@Destination
@Composable
fun ListTransactions() {
    var trigger by remember {
        mutableStateOf(true)
    }
    LaunchedEffect(Unit) { trigger = !trigger }
    val group = mapOf(
        "Today" to 3,
        "Yesterday" to 4,
    )
    Column {
        Header(trigger)
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(
                top = 15.dp,
                bottom = 140.dp,
                start = 15.dp,
                end = 15.dp
            ),
            modifier = Modifier
                .fillMaxHeight()
                .background(TransactionsLazyCol)
        ) {
            group.forEach { (day, size) ->
                stickyHeader {
                    Text(
                        text = day,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(TransactionsLazyCol)
                            .padding(10.dp),
                        fontFamily = DMSans,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }
                items(size) {
                    if (it == 1) {
                        TransactionCard(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            income = true
                        )
                    } else
                        TransactionCard(Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
    }
}
