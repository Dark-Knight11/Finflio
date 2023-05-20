package com.finflio.feature_transactions.presentation.unsettled_transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.finflio.core.presentation.navigation.HomeNavGraph
import com.finflio.destinations.TransactionInfoScreenDestination
import com.finflio.feature_transactions.presentation.add_edit_transactions.util.Categories
import com.finflio.feature_transactions.presentation.list_transactions.components.TransactionCard
import com.finflio.feature_transactions.presentation.unsettled_transactions.components.UnsettledTransactionTopAppBar
import com.finflio.ui.theme.AddTransferBg
import com.finflio.ui.theme.TransactionCardBg
import com.finflio.ui.theme.gradientBackground
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@HomeNavGraph
@Destination
@Composable
fun UnsettledTransactions(
    navigator: DestinationsNavigator,
    viewModel: UnsettledTransactionsViewModel = hiltViewModel()
) {
    val unsettledTransactions = viewModel.unsettledTransactions.value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .gradientBackground(
                colorStops = arrayOf(
                    0.0f to AddTransferBg.copy(0.9f),
                    0.2f to TransactionCardBg
                ),
                angle = -70f,
                extraY = -120f
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        UnsettledTransactionTopAppBar() {
            navigator.popBackStack()
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(
                top = 15.dp,
                bottom = 140.dp,
                start = 15.dp,
                end = 15.dp
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            items(unsettledTransactions) { transaction ->
                TransactionCard(
                    category = Categories.valueOf(transaction.category),
                    time = transaction.timestamp,
                    amount = transaction.amount,
                    from = transaction.from,
                    type = transaction.type,
                    to = transaction.to
                ) {
                    navigator.navigate(TransactionInfoScreenDestination(transactionId = transaction.transactionId))
                }
            }
        }
    }
}