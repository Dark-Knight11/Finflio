package com.finflio.feature_transactions.presentation.transaction_info

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.finflio.core.presentation.navigation.HomeNavGraph
import com.finflio.core.presentation.util.toPx
import com.finflio.destinations.AddEditTransactionScreenDestination
import com.finflio.destinations.DeleteConfirmationDestination
import com.finflio.feature_transactions.presentation.add_edit_transactions.components.OpenableImage
import com.finflio.feature_transactions.presentation.add_edit_transactions.util.Categories
import com.finflio.feature_transactions.presentation.transaction_info.components.EditButton
import com.finflio.feature_transactions.presentation.transaction_info.components.Header
import com.finflio.feature_transactions.presentation.transaction_info.components.TopAppBar
import com.finflio.feature_transactions.presentation.transaction_info.util.TransactionInfoEvent
import com.finflio.feature_transactions.presentation.transaction_info.util.TransactionInfoUiEvent
import com.finflio.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.result.ResultRecipient
import java.time.LocalDateTime
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@Destination
@HomeNavGraph
fun TransactionInfoScreen(
    navigator: DestinationsNavigator,
    resultNavigator: ResultBackNavigator<Boolean>,
    viewModel: TransactionInfoViewModel = hiltViewModel(),
    transactionId: String,
    unsettled: Boolean = false,
    resultRecipient: ResultRecipient<DeleteConfirmationDestination, Boolean>
) {
    val scrollState = rememberScrollState()
    val transaction = viewModel.transaction.value
    var infoBarPositionOffset by remember { mutableStateOf(0f) }
    var infoBarHeight by remember { mutableStateOf(0f) }

    // if I go to edit screen and come back then the gradient is not there anymore
    // this is happening due to some issue with side effect, not exactly sure whats going on
    // but making it remember by height as key fixes the issue ... mostly
    // new issue arises which is observed when you scroll the screen the gradient
    // goes up way to fast
    val infoBarPositionSnapshot = remember(infoBarPositionOffset) {
        infoBarPositionOffset
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest {
            when (it) {
                TransactionInfoUiEvent.NavigateBack -> {
                    resultNavigator.navigateBack(result = true)
                }
            }
        }
    }

    resultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> println("canceled!!")
            is NavResult.Value -> if (result.value) {
                viewModel.onEvent(TransactionInfoEvent.DeleteTransaction(transaction))
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
                modifier = Modifier.statusBarsPadding()
            )
        },
        backgroundColor = when (transaction?.type) {
            "Expense" -> ExpenseBG
            "Income" -> IncomeBG
            else -> TransferBg
        },
        bottomBar = {
            EditButton(
                modifier = Modifier
                    .padding(vertical = 25.dp, horizontal = 30.dp)
                    .navigationBarsPadding(),
                type = transaction?.type ?: "Expense"
            ) {
                navigator.navigate(
                    AddEditTransactionScreenDestination(
                        type = transaction?.type ?: "Expense",
                        transactionId = transactionId
                    )
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(
                    brush = Brush.radialGradient(
                        0.8f to when (transaction?.type) {
                            "Expense" -> ExpenseBG
                            "Income" -> IncomeBG
                            else -> TransferBg
                        },
                        1f to MainBackground,
                        radius = (2 * infoBarPositionSnapshot).coerceAtLeast(1f),
                        center = Offset(
                            screenSize.width.value.toPx / 2f,
                            -(infoBarPositionSnapshot - (infoBarHeight / 2))
                        )
                    )
                )
                .padding(bottom = navigationBottomBarHeight + 100.dp),
            horizontalAlignment = CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Header(
                amount = transaction?.amount ?: 0f,
                timestamp = transaction?.timestamp ?: LocalDateTime.now(),
                type = transaction?.type ?: "Expense",
                category = Categories.valueOf(transaction?.category ?: "Food"),
                paymentMethod = transaction?.paymentMethod ?: "Gpay",
                infoBarModifier = Modifier
                    .onGloballyPositioned {
                        infoBarPositionOffset = it.positionInWindow().y
                    }
                    .onSizeChanged {
                        infoBarHeight = it.height.toFloat()
                    }
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
                    OpenableImage(
                        modifier = Modifier
                            .aspectRatio(1.3f, true)
                            .clip(RoundedCornerShape(5.dp))
                            .border(1.dp, SecondaryText, RoundedCornerShape(5.dp))
                            .padding(10.dp)
                            .align(CenterHorizontally),
                        imageUrl = it
                    )
                }
            }
        }
    }
}
