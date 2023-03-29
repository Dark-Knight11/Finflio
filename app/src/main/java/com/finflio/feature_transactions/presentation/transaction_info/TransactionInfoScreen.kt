package com.finflio.feature_transactions.presentation.transaction_info

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.finflio.R
import com.finflio.core.presentation.navigation.HomeNavGraph
import com.finflio.destinations.DeleteConfirmationDestination
import com.finflio.feature_transactions.domain.model.Transaction
import com.finflio.feature_transactions.presentation.transaction_info.components.EditButton
import com.finflio.feature_transactions.presentation.transaction_info.components.Header
import com.finflio.feature_transactions.presentation.transaction_info.components.TopAppBar
import com.finflio.feature_transactions.presentation.transaction_info.util.TransactionInfoEvent
import com.finflio.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.result.ResultRecipient
import java.time.LocalDateTime

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@Destination
@HomeNavGraph
fun TransactionInfoScreen(
    navigator: DestinationsNavigator,
    resultNavigator: ResultBackNavigator<Transaction>,
    viewModel: TransactionInfoViewModel = hiltViewModel(),
    transactionId: Int,
    resultRecipient: ResultRecipient<DeleteConfirmationDestination, Boolean>
) {
    val scrollState = rememberScrollState()
    val transaction = viewModel.transaction.value
    resultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> println("canceled!!")
            is NavResult.Value -> if (result.value) {
                viewModel.onEvent(TransactionInfoEvent.DeleteTransaction(transaction))
                transaction?.let { resultNavigator.navigateBack(it) }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                onDelete = {
                    navigator.navigate(DeleteConfirmationDestination(transaction?.type))
                },
                onBackPress = { navigator.popBackStack() },
                modifier = Modifier.padding(top = 45.dp)
            )
        },
        backgroundColor = if (transaction?.type == "Expense") ExpenseBG else IncomeBG
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(MainBackground)
                .then(
                    if (transaction?.type == "Expense") {
                        Modifier
                            .background(
                                brush = Brush.radialGradient(
                                    0.8f to ExpenseBG,
                                    1f to Color.Transparent,
                                    radius = 1500f,
                                    center = Offset(500f, -400f)
                                )
                            )
                    } else {
                        Modifier
                            .background(
                                brush = Brush.radialGradient(
                                    0.8f to IncomeBG,
                                    1f to Color.Transparent,
                                    radius = 1500f,
                                    center = Offset(500f, -400f)
                                )
                            )
                    }
                )
                .padding(bottom = navigationBarHeight),
            horizontalAlignment = CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Header(
                amount = transaction?.amount ?: 0f,
                timestamp = transaction?.timestamp ?: LocalDateTime.now(),
                type = transaction?.type ?: "Expense",
                category = transaction?.category ?: "Other",
                paymentMethod = transaction?.paymentMethod ?: "Gpay"
            )
            Spacer(modifier = Modifier.height(15.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Description",
                    fontFamily = DMSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White
                )
                Text(
                    text = transaction?.description ?: "",
                    fontFamily = DMSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.White
                )
                transaction?.attachment?.let {
                    Text(
                        text = "Attachment",
                        fontFamily = DMSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                    Image(
                        painter = painterResource(id = R.drawable.sample),
                        contentDescription = "bill",
                        modifier = Modifier
                            .padding(10.dp)
                            .align(CenterHorizontally)
                    )
                }
            }
            EditButton(
                modifier = Modifier
                    .padding(vertical = 25.dp, horizontal = 30.dp),
                type = transaction?.type ?: "Expense"
            )
        }
    }
}