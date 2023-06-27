package com.finflio.feature_transactions.presentation.list_transactions

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.finflio.R
import com.finflio.core.presentation.components.CommonSnackBar
import com.finflio.core.presentation.components.PullRefresh
import com.finflio.core.presentation.navigation.HomeNavGraph
import com.finflio.destinations.AddEditTransactionScreenDestination
import com.finflio.destinations.TransactionInfoScreenDestination
import com.finflio.destinations.UnsettledTransactionsDestination
import com.finflio.feature_transactions.domain.model.TransactionModel
import com.finflio.feature_transactions.presentation.add_edit_transactions.util.Categories
import com.finflio.feature_transactions.presentation.list_transactions.components.Header
import com.finflio.feature_transactions.presentation.list_transactions.components.TransactionCard
import com.finflio.feature_transactions.presentation.list_transactions.util.TransactionEvent
import com.finflio.feature_transactions.presentation.list_transactions.util.TransactionUiEvent
import com.finflio.ui.theme.DMSans
import com.finflio.ui.theme.Poppins
import com.finflio.ui.theme.TransactionsLazyCol
import com.finflio.ui.theme.TransferBlue
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Month

@SuppressLint("CoroutineCreationDuringComposition")
@HomeNavGraph(start = true)
@Destination
@Composable
fun ListTransactions(
    navigator: DestinationsNavigator,
    viewModel: ListTransactionsViewModel = hiltViewModel(),
    addEditTransactionScreenResultRecipient: ResultRecipient<AddEditTransactionScreenDestination, Boolean>,
    transactionInfoScreenResultRecipient: ResultRecipient<TransactionInfoScreenDestination, Boolean>
) {
    val transactions = viewModel.transactions.collectAsLazyPagingItems()
    val isRefreshing = viewModel.isRefreshing.value
    val monthTotal = viewModel.monthTotal.value
    val month = viewModel.month.value
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // TODO FIX not getting called
    addEditTransactionScreenResultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> println("No result!!")
            is NavResult.Value -> {
                if (result.value) {
                    viewModel.paginatedTransactions(
                        Month.valueOf(month.uppercase())
                    )
                }
            }
        }
    }

    transactionInfoScreenResultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> println("No result!!")
            is NavResult.Value -> {
                if (result.value) {
                    viewModel.paginatedTransactions(Month.valueOf(month.uppercase()))
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest {
            when (it) {
                is TransactionUiEvent.ShowSnackbar -> {
                    scope.launch { snackbarHostState.showSnackbar(it.message) }
                }
            }
        }
    }

    CommonSnackBar(
        snackBarHostState = snackbarHostState,
        modifier = Modifier.padding(bottom = 130.dp)
    ) {
        PullRefresh(
            refreshing = isRefreshing,
            onRefresh = {
                viewModel.paginatedTransactions(
                    Month.valueOf(month.uppercase())
                )
            },
            enabled = true
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Header(
                    total = monthTotal,
                    month = month,
                    onSelect = {
                        viewModel.onEvent(TransactionEvent.ChangeMonth(it))
                    },
                    onClick = { viewModel.logout() }
                )
                Box(modifier = Modifier.background(TransactionsLazyCol)) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(
                            top = 15.dp,
                            bottom = 140.dp,
                            start = 15.dp,
                            end = 15.dp
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                            .background(TransactionsLazyCol)
                    ) {
                        if (transactions.itemCount == 0) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillParentMaxSize()
                                        .padding(top = 50.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(
                                            id = R.drawable.ic_add_transaction_state
                                        ),
                                        contentDescription = "empty",
                                        modifier = Modifier.padding(bottom = 100.dp)
                                    )
                                }
                            }
                        } else {
                            items(transactions.itemCount) {
                                when (val transactionModel = transactions[it]) {
                                    is TransactionModel.TransactionItem -> {
                                        val transaction = transactionModel.transaction
                                        TransactionCard(
                                            modifier = Modifier.align(Alignment.Center),
                                            category = Categories.valueOf(transaction.category),
                                            amount = transaction.amount,
                                            time = transaction.timestamp,
                                            to = transaction.to,
                                            from = transaction.from,
                                            type = transaction.type
                                        ) {
                                            navigator.navigate(
                                                TransactionInfoScreenDestination(
                                                    transaction.transactionId
                                                )
                                            )
                                        }
                                    }

                                    is TransactionModel.SeparatorItem -> {
                                        Text(
                                            text = transactionModel.separator,
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

                                    null -> {}
                                }
                            }
                        }
                        transactions.apply {
                            when {
                                loadState.refresh is LoadState.Loading -> {
                                    viewModel.isRefreshing.value = true
                                }

                                loadState.refresh is LoadState.NotLoading -> {
                                    viewModel.isRefreshing.value = false
                                }

                                loadState.append is LoadState.Loading -> {
                                    item { CircularProgressIndicator() }
                                }

                                loadState.refresh is LoadState.Error -> {
                                    viewModel.isRefreshing.value = false
                                    val e = transactions.loadState.refresh as LoadState.Error
                                    Log.i("List Transaction Screen", e.toString())
//                                    if (!e.toString().contains("failed to connect")) {
//                                        viewModel.onUiEvent(
//                                            TransactionUiEvent.ShowSnackbar(
//                                                e.error.message.toString()
//                                            )
//                                        )
//                                    } else {
//                                        viewModel.onUiEvent(
//                                            TransactionUiEvent.ShowSnackbar("Something went wrong")
//                                        )
//                                    }
                                }

                                loadState.append is LoadState.Error -> {
                                    viewModel.isRefreshing.value = false
                                    val e = transactions.loadState.append as LoadState.Error
                                    Log.i("List Transaction Screen", e.toString())
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .graphicsLayer {
                                shape = RoundedCornerShape(bottomStart = 10.dp)
                                clip = true
                            }
                            .clickable {
                                navigator.navigate(UnsettledTransactionsDestination)
                            }
                            .background(TransferBlue.copy(0.5f))
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Unsettled",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontFamily = Poppins,
                            fontSize = 14.sp
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "transfers",
                            tint = Color.White,
                            modifier = Modifier
                                .size(22.dp)
                                .padding(bottom = 3.dp)
                        )
                    }
                }
            }
        }
    }
}
