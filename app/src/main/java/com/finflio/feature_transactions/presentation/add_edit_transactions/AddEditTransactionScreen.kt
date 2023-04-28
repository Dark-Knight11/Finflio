package com.finflio.feature_transactions.presentation.add_edit_transactions

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.finflio.R
import com.finflio.core.presentation.components.CommonSnackBar
import com.finflio.core.presentation.navigation.MainNavGraph
import com.finflio.feature_transactions.presentation.add_edit_transactions.components.*
import com.finflio.feature_transactions.presentation.add_edit_transactions.util.AddEditTransactionEvent
import com.finflio.feature_transactions.presentation.add_edit_transactions.util.AddEditTransactionUiEvent
import com.finflio.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.flow.collectLatest
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Destination
@MainNavGraph
@Composable
fun AddEditTransactionScreen(
    navigator: DestinationsNavigator,
    type: String,
    transactionId: Int = 0,
    viewModel: AddEditTransactionViewModel = hiltViewModel()
) {
    val formatter = DateTimeFormatter.ofPattern("K:mm a - MMM d, yyyy")
    val formattedDateTime = viewModel.timestamp.value.format(formatter)
    val amount = viewModel.amount.value
    val to = viewModel.to.value
    val from = viewModel.from.value
    val description = viewModel.description.value
    val category = viewModel.category.value
    val paymentMethod = viewModel.paymentMethod.value
    val imgUri = viewModel.imgUri.value
    val attachment = viewModel.attachment.value

    val scrollState = rememberScrollState()
    val dateTimePicker = rememberMaterialDialogState()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                uri?.let {
                    viewModel.onEvent(
                        AddEditTransactionEvent.ChangeImagePath(it)
                    )
                }
            })
    var showLoader by rememberSaveable { mutableStateOf(false) }

    DateTimePicker(dateTimePicker, initialDateTime = viewModel.timestamp.value) {
        viewModel.onEvent(AddEditTransactionEvent.ChangeTimestamp(it))
    }

    LaunchedEffect(true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditTransactionUiEvent.NavigateBack -> {
                    if (transactionId != 0)
                        navigator.popBackStack("list_transactions", false)
                    else navigator.popBackStack()
                }

                is AddEditTransactionUiEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                is AddEditTransactionUiEvent.HideLoader -> {
                    showLoader = false
                }

                is AddEditTransactionUiEvent.ShowLoader -> {
                    showLoader = true
                }
            }
        }
    }

    CommonSnackBar(
        snackBarHostState = snackbarHostState,
        modifier = Modifier.padding(bottom = navigationBarHeight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .gradientBackground(
                    colorStops = arrayOf(
                        0.0f to if (type == "Expense") AddExpenseBG.copy(0.9f)
                        else AddIncomeBG.copy(0.9f),
                        0.2f to TransactionCardBg
                    ),
                    angle = -70f,
                    extraY = -120f
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            AddEditTransactionTopAppBar(type = type) { navigator.popBackStack() }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            0.0f to if (type == "Expense") AddExpenseBG.copy(0.5f)
                            else AddIncomeBG.copy(0.5f),
                            1f to Color.Transparent,
                            radius = 1700f,
                            center = Offset(2000f, 1000f)
                        )
                    )
                    .verticalScroll(scrollState)
                    .padding(top = 40.dp, start = 15.dp, end = 15.dp, bottom = 50.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InputCard(title = "Transaction") {
                    CustomTextField(
                        value = formattedDateTime,
                        readOnly = true,
                        onValueChange = { },
                        interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        dateTimePicker.show()
                                    }
                                }
                            }
                        },
                    )
                }

                InputCard(title = "Amount") {
                    CustomTextField(
                        value = amount.removeSuffix(".0"),
                        onValueChange = {
                            viewModel.onEvent(
                                AddEditTransactionEvent.ChangeAmount(it)
                            )
                        },
                        placeholder = {
                            Text(
                                text = "0",
                                fontFamily = DMSans,
                                fontSize = 15.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_rupee),
                                contentDescription = "amount",
                                tint = SecondaryText
                            )
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }),
                    )
                }

                CategoryDropDown(category) {
                    viewModel.onEvent(AddEditTransactionEvent.ChangeCategory(it))
                }

                PaymentMethodDropdown(paymentMethod) {
                    viewModel.onEvent(AddEditTransactionEvent.ChangePaymentMethod(it))
                }

                if (type == "Expense") {
                    InputCard(title = "To") {
                        CustomTextField(
                            value = to,
                            placeholder = {
                                Text(
                                    text = "To",
                                    fontFamily = DMSans,
                                    fontSize = 15.sp
                                )
                            },
                            onValueChange = {
                                viewModel.onEvent(
                                    AddEditTransactionEvent.ChangeTo(it)
                                )
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }),
                        )
                    }
                } else {
                    InputCard(title = "From") {
                        CustomTextField(
                            value = from,
                            placeholder = {
                                Text(
                                    text = "From",
                                    fontFamily = DMSans,
                                    fontSize = 15.sp
                                )
                            },
                            onValueChange = {
                                viewModel.onEvent(
                                    AddEditTransactionEvent.ChangeFrom(it)
                                )
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }),
                        )
                    }
                }

                InputCard(title = "Description") {
                    CustomTextField(
                        value = description,
                        placeholder = {
                            Text(
                                text = "Description",
                                fontFamily = DMSans,
                                fontSize = 15.sp
                            )
                        },
                        onValueChange = {
                            viewModel.onEvent(
                                AddEditTransactionEvent.ChangeDescription(it)
                            )
                        },
                        singleLine = false
                    )
                }
                if (imgUri == null) {
                    if (!attachment.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(0.dp))
                        ImageItem(
                            modifier = Modifier
                                .size(200.dp)
                                .align(Alignment.CenterHorizontally),
                            link = attachment
                        ) { viewModel.onEvent(AddEditTransactionEvent.ChangeImagePath(Uri.EMPTY)) }
                    }
                    else {
                        AddImageCard() {
                            imagePickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    }
                } else {
                    if (imgUri != Uri.EMPTY) {
                        Spacer(modifier = Modifier.height(0.dp))
                        ImageItem(
                            modifier = Modifier
                                .size(200.dp)
                                .align(Alignment.CenterHorizontally),
                            uri = imgUri
                        ) { viewModel.onEvent(AddEditTransactionEvent.ChangeImagePath(null)) }
                    } else {
                        AddImageCard() {
                            imagePickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    }
                }

                SaveCancelButtons(
                    modifier = Modifier.fillMaxWidth(),
                    type = type,
                    showLoader = showLoader,
                    onCancel = { viewModel.onEvent(AddEditTransactionEvent.CancelTransaction) },
                    onSave = {
                        if (transactionId == 0)
                            viewModel.onEvent(AddEditTransactionEvent.AddTransactionEvent(context))
                        else viewModel.onEvent(AddEditTransactionEvent.EditTransactionEvent(context))
                    }
                )
            }
        }
    }
}






