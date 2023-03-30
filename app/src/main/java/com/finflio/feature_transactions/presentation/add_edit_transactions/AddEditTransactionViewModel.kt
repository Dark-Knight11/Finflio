package com.finflio.feature_transactions.presentation.add_edit_transactions

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finflio.feature_transactions.domain.model.Transaction
import com.finflio.feature_transactions.domain.use_case.TransactionUseCases
import com.finflio.feature_transactions.domain.util.InvalidTransactionException
import com.finflio.feature_transactions.presentation.add_edit_transactions.util.AddExpenseEvent
import com.finflio.feature_transactions.presentation.add_edit_transactions.util.AddExpenseUiEvent
import com.finflio.feature_transactions.presentation.add_edit_transactions.util.Categories
import com.finflio.feature_transactions.presentation.add_edit_transactions.util.PaymentMethods
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddEditTransactionViewModel @Inject constructor(
    private val useCase: TransactionUseCases
) : ViewModel() {
    private val _timestamp = mutableStateOf<LocalDateTime>(LocalDateTime.now())
    val timestamp: State<LocalDateTime> = _timestamp

    private val _category = mutableStateOf<Categories>(Categories.Food)
    val category: State<Categories> = _category

    private val _amount = mutableStateOf<String>("")
    val amount: State<String> = _amount

    private val _to = mutableStateOf<String>("")
    val to: State<String> = _to

    private val _description = mutableStateOf<String>("")
    val description: State<String> = _description

    private val _paymentMethod = mutableStateOf<PaymentMethods>(PaymentMethods.GPay)
    val paymentMethod: State<PaymentMethods> = _paymentMethod

    val eventFlow = MutableSharedFlow<AddExpenseUiEvent>()

    fun onEvent(event: AddExpenseEvent) {
        when (event) {
            is AddExpenseEvent.ChangeTimestamp -> {
                _timestamp.value = event.timestamp
            }
            is AddExpenseEvent.ChangeAmount -> {
                _amount.value = event.amount
            }
            is AddExpenseEvent.ChangeCategory -> {
                _category.value = event.categories
            }
            is AddExpenseEvent.ChangeDescription -> {
                _description.value = event.description
            }
            is AddExpenseEvent.ChangeTo -> {
                _to.value = event.to
            }
            is AddExpenseEvent.ChangePaymentMethod -> {
                _paymentMethod.value = event.paymentMethods
            }
            is AddExpenseEvent.EditTransactionEvent -> {
                viewModelScope.launch {
                    try {
                        useCase.updateTransactionUseCase(
                            Transaction(
                                transactionId = 1, // TODO fetch id from nav args
                                timestamp = timestamp.value,
                                type = "Expense",
                                category = category.value.category,
                                paymentMethod = paymentMethod.value.method,
                                description = description.value,
                                amount = amount.value.toFloat(),
                                attachment = "",
                                to = to.value
                            )
                        )
                    } catch (e: InvalidTransactionException) {
                        Log.e("EditExpense", e.message.toString())
                    }
                }
            }
            is AddExpenseEvent.AddTransactionEvent -> {
                if (amount.value.isNotBlank()) {
                    viewModelScope.launch {
                        try {
                            if (useCase.addTransactionUseCase(
                                    Transaction(
                                        timestamp = timestamp.value,
                                        type = "Expense",
                                        category = category.value.category,
                                        paymentMethod = paymentMethod.value.method,
                                        description = description.value,
                                        amount = amount.value.toFloat(),
                                        attachment = "",
                                        to = to.value
                                    )
                                )
                            ) eventFlow.emit(AddExpenseUiEvent.NavigateBack)
                        } catch (e: InvalidTransactionException) {
                            Log.e("AddExpense", e.message.toString())
                            eventFlow.emit(
                                AddExpenseUiEvent.ShowSnackBar(
                                    e.message ?: "Unexpected Error Occurred. Please Try Again"
                                )
                            )
                        }
                    }
                } else {
                    viewModelScope.launch {
                        eventFlow.emit(
                            AddExpenseUiEvent.ShowSnackBar("Amount can't be empty. Please Enter Valid Amount")
                        )
                    }
                }
            }
            is AddExpenseEvent.CancelTransaction -> {
                _timestamp.value = LocalDateTime.now()
                _category.value = Categories.Food
                _to.value = ""
                _amount.value = ""
                _paymentMethod.value = PaymentMethods.GPay
                _description.value = ""
            }
        }
    }


}
