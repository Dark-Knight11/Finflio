package com.finflio.feature_transactions.presentation.list_transactions

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.finflio.core.presentation.components.CommonSnackBar
import com.finflio.core.presentation.navigation.HomeNavGraph
import com.finflio.destinations.TransactionInfoScreenDestination
import com.finflio.feature_transactions.domain.model.Transaction
import com.finflio.feature_transactions.presentation.add_edit_transactions.util.Categories
import com.finflio.feature_transactions.presentation.list_transactions.components.Header
import com.finflio.feature_transactions.presentation.list_transactions.components.TransactionCard
import com.finflio.feature_transactions.presentation.list_transactions.util.TransactionEvent
import com.finflio.ui.theme.DMSans
import com.finflio.ui.theme.TransactionsLazyCol
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@HomeNavGraph(start = true)
@Destination
@Composable
fun ListTransactions(
    navigator: DestinationsNavigator,
    viewModel: ListTransactionViewModel = hiltViewModel(),
    resultRecipient: ResultRecipient<TransactionInfoScreenDestination, Transaction>
) {
    val transactions = viewModel.transactions.value
    val monthTotal = viewModel.monthTotal.value
    var trigger by remember {
        mutableStateOf(true)
    }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) { trigger = !trigger }

    resultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> println("No result!!")
            is NavResult.Value -> {
                scope.launch {
                    val undo = snackbarHostState.showSnackbar(
                        message = "Note Deleted Successfully",
                        actionLabel = "Undo",
                        duration = SnackbarDuration.Short
                    )
                    if (undo == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(TransactionEvent.RestoreTransaction(result.value))
                    }
                }
            }
        }
    }
    CommonSnackBar(
        snackBarHostState = snackbarHostState,
        modifier = Modifier.padding(bottom = 130.dp)
    ) {
        Column {
            Header(trigger, monthTotal) {
                viewModel.onEvent(TransactionEvent.ChangeMonth(it))
            }
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
                transactions.forEach { (day, transactions) ->
                    stickyHeader {
                        if (transactions.isNotEmpty()) {
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
                    }
                    items(transactions) { transaction ->
                        TransactionCard(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            category = Categories.valueOf(transaction.category),
                            amount = transaction.amount,
                            time = transaction.timestamp,
                            to = transaction.to,
                            from = transaction.from,
                            type = transaction.type
                        ) { navigator.navigate(TransactionInfoScreenDestination(transaction.transactionId)) }
                    }
                }
            }
        }
    }
}