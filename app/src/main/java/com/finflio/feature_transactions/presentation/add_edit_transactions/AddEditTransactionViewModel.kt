package com.finflio.feature_transactions.presentation.add_edit_transactions

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finflio.feature_transactions.domain.model.Transaction
import com.finflio.feature_transactions.domain.use_case.TransactionUseCases
import com.finflio.feature_transactions.domain.util.InvalidTransactionException
import com.finflio.feature_transactions.presentation.add_edit_transactions.util.AddEditTransactionEvent
import com.finflio.feature_transactions.presentation.add_edit_transactions.util.AddEditTransactionUiEvent
import com.finflio.feature_transactions.presentation.add_edit_transactions.util.Categories
import com.finflio.feature_transactions.presentation.add_edit_transactions.util.PaymentMethods
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddEditTransactionViewModel @Inject constructor(
    private val useCase: TransactionUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _transactionId = mutableStateOf<Int>(0)
    val transactionId: State<Int> = _transactionId

    private val _timestamp = mutableStateOf<LocalDateTime>(LocalDateTime.now())
    val timestamp: State<LocalDateTime> = _timestamp

    private val _category = mutableStateOf<Categories>(Categories.Food)
    val category: State<Categories> = _category

    private val _amount = mutableStateOf<String>("")
    val amount: State<String> = _amount

    private val _to = mutableStateOf<String>("")
    val to: State<String> = _to

    private val _from = mutableStateOf<String>("")
    val from: State<String> = _from

    private val _type = mutableStateOf<String>("")
    val type: State<String> = _type

    private val _description = mutableStateOf<String>("")
    val description: State<String> = _description

    private val _paymentMethod = mutableStateOf<PaymentMethods>(PaymentMethods.GPay)
    val paymentMethod: State<PaymentMethods> = _paymentMethod

    val eventFlow = MutableSharedFlow<AddEditTransactionUiEvent>()

    init {
        viewModelScope.launch {
            savedStateHandle.get<String>("type")?.let {
                _type.value = it
            }
            savedStateHandle.get<Int>("transactionId")?.let {
                _transactionId.value = it
            }
            if (transactionId.value != 0) {
                useCase.getTransactionUseCase(transactionId.value).also {
                    _type.value = it.type
                    _amount.value = it.amount.toString()
                    _paymentMethod.value = PaymentMethods.valueOf(it.paymentMethod)
                    _category.value = Categories.valueOf(it.category)
                    _description.value = it.description
                    _to.value = it.to ?: ""
                    _from.value = it.from ?: ""
                    _timestamp.value = it.timestamp
                }
            }
        }
    }

    fun onEvent(event: AddEditTransactionEvent) {
        when (event) {
            is AddEditTransactionEvent.ChangeTimestamp -> {
                _timestamp.value = event.timestamp
            }
            is AddEditTransactionEvent.ChangeAmount -> {
                _amount.value = event.amount
            }
            is AddEditTransactionEvent.ChangeCategory -> {
                _category.value = event.categories
            }
            is AddEditTransactionEvent.ChangeDescription -> {
                _description.value = event.description
            }
            is AddEditTransactionEvent.ChangeTo -> {
                _to.value = event.to
            }
            is AddEditTransactionEvent.ChangeFrom -> {
                _from.value = event.from
            }
            is AddEditTransactionEvent.ChangePaymentMethod -> {
                _paymentMethod.value = event.paymentMethods
            }
            is AddEditTransactionEvent.EditTransactionEvent -> {
                viewModelScope.launch {
                    try {
                        if (
                            useCase.updateTransactionUseCase(
                                Transaction(
                                    transactionId = transactionId.value,
                                    timestamp = timestamp.value,
                                    type = type.value,
                                    category = category.value.category,
                                    paymentMethod = paymentMethod.value.method,
                                    description = description.value,
                                    amount = amount.value.toFloat(),
                                    to = to.value,
                                    from = from.value
                                )
                            )
                        ) eventFlow.emit(AddEditTransactionUiEvent.NavigateBack)
                    } catch (e: InvalidTransactionException) {
                        eventFlow.emit(
                            AddEditTransactionUiEvent.ShowSnackBar(
                                e.message ?: "Unexpected Error Occurred. Please Try Again"
                            )
                        )
                    }
                }
            }
            is AddEditTransactionEvent.AddTransactionEvent -> {
                if (amount.value.isNotBlank()) {
                    viewModelScope.launch {
                        try {
                            if (useCase.addTransactionUseCase(
                                    Transaction(
                                        timestamp = timestamp.value,
                                        type = type.value,
                                        category = category.value.category,
                                        paymentMethod = paymentMethod.value.method,
                                        description = description.value,
                                        amount = amount.value.toFloat(),
                                        to = to.value,
                                        from = from.value
                                    )
                                )
                            ) eventFlow.emit(AddEditTransactionUiEvent.NavigateBack)
                        } catch (e: InvalidTransactionException) {
                            eventFlow.emit(
                                AddEditTransactionUiEvent.ShowSnackBar(
                                    e.message ?: "Unexpected Error Occurred. Please Try Again"
                                )
                            )
                        }
                    }
                } else {
                    viewModelScope.launch {
                        eventFlow.emit(
                            AddEditTransactionUiEvent.ShowSnackBar("Amount can't be empty. Please Enter Valid Amount")
                        )
                    }
                }
            }
            is AddEditTransactionEvent.CancelTransaction -> {
                viewModelScope.launch {
                    eventFlow.emit(AddEditTransactionUiEvent.NavigateBack)
                }
            }
        }
    }


}
