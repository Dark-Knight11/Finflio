package com.finflio.feature_transactions.presentation.unsettled_transactions

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.finflio.R
import com.finflio.core.presentation.components.PullRefresh
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
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@HomeNavGraph
@Destination
@Composable
fun UnsettledTransactions(
    navigator: DestinationsNavigator,
    viewModel: UnsettledTransactionsViewModel = hiltViewModel(),
    resultRecipient: ResultRecipient<TransactionInfoScreenDestination, Boolean>
) {
    val unsettledTransactions = viewModel.unsettledTransactions.collectAsLazyPagingItems()
    val refreshing = viewModel.refreshing.value
    resultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> println("No result!!")
            is NavResult.Value -> {
                viewModel.refreshData()
            }
        }
    }
    PullRefresh(
        refreshing = refreshing,
        onRefresh = {
            viewModel.refreshData()
        },
        enabled = false
    ) {
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
                if (unsettledTransactions.itemCount == 0) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(
                                    id = R.drawable.ic_no_unsettled_transactions_state
                                ),
                                contentDescription = "empty"
                            )
                        }
                    }
                } else {
                    items(unsettledTransactions.itemCount) { index ->
                        val transaction = unsettledTransactions[index]
                        transaction?.let {
                            TransactionCard(
                                category = Categories.valueOf(transaction.category),
                                time = transaction.timestamp,
                                amount = transaction.amount,
                                from = transaction.from,
                                type = transaction.type,
                                to = transaction.to
                            ) {
                                navigator.navigate(
                                    TransactionInfoScreenDestination(
                                        transactionId = transaction.transactionId,
                                        unsettled = true
                                    )
                                )
                            }
                        }
                    }
                }
                unsettledTransactions.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            viewModel.refreshing.value = true
                        }

                        loadState.refresh is LoadState.NotLoading -> {
                            viewModel.refreshing.value = false
                        }

                        loadState.append is LoadState.Loading -> {
                            item { CircularProgressIndicator() }
                        }

                        loadState.refresh is LoadState.Error -> {
                            viewModel.refreshing.value = false
                            val e = unsettledTransactions.loadState.refresh as LoadState.Error
                            Log.i("Unsettled Transaction Screen", e.toString())
                        }

                        loadState.append is LoadState.Error -> {
                            viewModel.refreshing.value = false
                            val e = unsettledTransactions.loadState.append as LoadState.Error
                            Log.i("Unsettled Transaction Screen", e.toString())
                        }
                    }
                }
            }
        }
    }
}
