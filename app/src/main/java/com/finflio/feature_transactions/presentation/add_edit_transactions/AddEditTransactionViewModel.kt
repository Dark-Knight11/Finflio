package com.finflio.feature_transactions.presentation.add_edit_transactions

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.finflio.core.domain.model.Transaction
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

    private val _description = mutableStateOf<String>("This is description for ${category.value}")
    val description: State<String> = _description

    private val _imgUri = mutableStateOf<Uri?>(null)
    val imgUri: State<Uri?> = _imgUri

    private val _attachment = mutableStateOf<String?>(null)
    val attachment: State<String?> = _attachment

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
                    _attachment.value = it.attachment
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
                _category.value = event.category
                _description.value = "This is description for ${category.value}"
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

            is AddEditTransactionEvent.ChangeImagePath -> {
                _imgUri.value = event.imgUri
            }

            is AddEditTransactionEvent.ChangePaymentMethod -> {
                _paymentMethod.value = event.paymentMethods
            }

            is AddEditTransactionEvent.CancelTransaction -> {
                viewModelScope.launch {
                    eventFlow.emit(AddEditTransactionUiEvent.NavigateBack)
                }
            }

            is AddEditTransactionEvent.EditTransactionEvent -> {
                // TODO Fix Someday
                // if you edit a transaction and you try to change the existing image in it
                // and if some error occurs at the time like amount can't be negative then
                // old image is deleted from cloudinary. So if user cancels the transaction
                // after that error then previous image of that transaction is deleted from
                // cloud but not from database and this causes issue
                // Hopefully this will be fixed once separate backend is integrated
                if (amount.value.isNotBlank()) {
                    viewModelScope.launch {
                        if (imgUri.value != null && attachment.value.isNullOrBlank()) {
                            uploadImage(imgUri.value, event.context, event)
                        } else if (imgUri.value != null && imgUri.value != Uri.EMPTY && !attachment.value.isNullOrBlank()) {
                            useCase.deleteImageUseCase(attachment.value)
                            uploadImage(imgUri.value, event.context, event)
                        } else {
                            if (imgUri.value == Uri.EMPTY && !attachment.value.isNullOrBlank()) {
                                useCase.deleteImageUseCase(attachment.value)
                                addTransaction(event)
                            } else addTransaction(event, attachment.value)
                        }
                    }
                } else {
                    viewModelScope.launch {
                        eventFlow.emit(
                            AddEditTransactionUiEvent.ShowSnackBar(
                                "Amount Can't be blank"
                            )
                        )
                    }
                }
            }

            is AddEditTransactionEvent.AddTransactionEvent -> {
                if (amount.value.isNotBlank()) {
                    if (imgUri.value != null)
                        uploadImage(imgUri.value, event.context, event)
                    else
                        addTransaction(event)
                } else {
                    viewModelScope.launch {
                        eventFlow.emit(
                            AddEditTransactionUiEvent.ShowSnackBar(
                                "Amount Can't be blank"
                            )
                        )
                    }
                }
            }
        }
    }

    private fun uploadImage(uri: Uri?, context: Context, event: AddEditTransactionEvent) {
        viewModelScope.launch {
            MediaManager.get()
                .upload(uri)
                .option("folder", "finflio")
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String?) {
                        viewModelScope.launch {
                            eventFlow.emit(AddEditTransactionUiEvent.ShowLoader)
                        }
                    }

                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                        Log.i(
                            "AddEditTransactionViewModel",
                            "Cloudinary Image Upload Progress: $bytes/$totalBytes"
                        )
                    }

                    override fun onSuccess(
                        requestId: String?,
                        resultData: MutableMap<Any?, Any?>?
                    ) {
                        viewModelScope.launch {
                            eventFlow.emit(AddEditTransactionUiEvent.HideLoader)
                        }
                        addTransaction(event, resultData?.get("secure_url").toString())
                    }

                    override fun onError(requestId: String?, error: ErrorInfo?) {
                        viewModelScope.launch {
                            eventFlow.emit(AddEditTransactionUiEvent.HideLoader)
                            eventFlow.emit(
                                AddEditTransactionUiEvent.ShowSnackBar(
                                    error?.description
                                        ?: "Unexpected Error Occurred. Please Try Again"
                                )
                            )
                        }
                    }

                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                        viewModelScope.launch {
                            eventFlow.emit(AddEditTransactionUiEvent.HideLoader)
                            eventFlow.emit(
                                AddEditTransactionUiEvent.ShowSnackBar(
                                    error?.description
                                        ?: "Unexpected Error Occurred. Please Try Again"
                                )
                            )
                        }
                    }

                }).startNow(context)
        }
    }

    private fun addTransaction(event: AddEditTransactionEvent, imgUrl: String? = null) {
        val transaction = Transaction(
            timestamp = timestamp.value,
            type = type.value,
            category = category.value.category,
            paymentMethod = paymentMethod.value.method,
            description = description.value,
            amount = amount.value.toFloat(),
            to = to.value,
            from = from.value,
            attachment = if (imgUrl.isNullOrBlank()) null else imgUrl
        )
        when (event) {
            is AddEditTransactionEvent.AddTransactionEvent -> {
                viewModelScope.launch {
                    try {
                        if (useCase.addTransactionUseCase(transaction))
                            eventFlow.emit(AddEditTransactionUiEvent.NavigateBack)
                    } catch (e: InvalidTransactionException) {
                        if (!imgUrl.isNullOrBlank()) useCase.deleteImageUseCase(imgUrl)
                        eventFlow.emit(
                            AddEditTransactionUiEvent.ShowSnackBar(
                                e.message ?: "Unexpected Error Occurred. Please Try Again"
                            )
                        )
                    }
                }
            }

            is AddEditTransactionEvent.EditTransactionEvent -> {
                viewModelScope.launch {
                    try {
                        if (useCase.updateTransactionUseCase(transaction.copy(transactionId = transactionId.value)))
                            eventFlow.emit(AddEditTransactionUiEvent.NavigateBack)
                    } catch (e: InvalidTransactionException) {
                        eventFlow.emit(
                            AddEditTransactionUiEvent.ShowSnackBar(
                                e.message ?: "Unexpected Error Occurred. Please Try Again"
                            )
                        )
                        if (
                            (!imgUrl.isNullOrBlank() && attachment.value.isNullOrBlank()) ||
                            (imgUrl != attachment.value && !attachment.value.isNullOrBlank())
                        ) {
                            useCase.deleteImageUseCase(imgUrl)
                        }
                    }
                }
            }

            else -> {}
        }
    }
}