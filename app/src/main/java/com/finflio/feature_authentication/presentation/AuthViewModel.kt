package com.finflio.feature_authentication.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finflio.core.data.network.resource.Resource
import com.finflio.feature_authentication.domain.use_case.LoginUseCase
import com.finflio.feature_authentication.domain.use_case.RegisterUseCase
import com.finflio.feature_authentication.domain.util.InvalidAuthRequestException
import com.finflio.feature_authentication.presentation.utils.AuthEvents
import com.finflio.feature_authentication.presentation.utils.AuthUiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    companion object {
        const val TAG = "AuthViewModel"
    }

    val eventFlow = MutableSharedFlow<AuthUiEvents>()
    fun onEvent(authEvents: AuthEvents) {
        viewModelScope.launch {
            when (authEvents) {
                is AuthEvents.Login -> {
                    try {
                        loginUseCase.login(authEvents.email, authEvents.password).collectLatest {
                            when (it.status) {
                                Resource.Status.SUCCESS -> {
                                    Log.i(TAG, it.data.toString())
                                }

                                Resource.Status.ERROR -> {
                                    Log.i(TAG, it.message.toString())
                                    eventFlow.emit(AuthUiEvents.ShowSnackbar(it.message.toString()))
                                }

                                Resource.Status.LOADING -> {
                                    println("loading...")
                                }
                            }
                        }
                    } catch (e: InvalidAuthRequestException) {
                        Log.e(TAG, e.message.toString())
                        eventFlow.emit(
                            AuthUiEvents.ShowSnackbar(
                                e.message ?: "Something went wrong"
                            )
                        )
                    }
                }

                is AuthEvents.Register -> {
                    try {
                        registerUseCase.register(
                            authEvents.name,
                            authEvents.email,
                            authEvents.password,
                            authEvents.confirmPassword
                        ).collectLatest {
                            when (it.status) {
                                Resource.Status.SUCCESS -> {
                                    Log.i(TAG, it.data.toString())
                                    eventFlow.emit(AuthUiEvents.NavigateBack)
                                }

                                Resource.Status.ERROR -> {
                                    Log.i(TAG + "Error", it.data?.message.toString())
                                    eventFlow.emit(AuthUiEvents.ShowSnackbar(it.message.toString()))
                                }

                                Resource.Status.LOADING -> {
                                    println("loading...")
                                }
                            }
                        }
                    } catch (e: InvalidAuthRequestException) {
                        Log.e(TAG, e.message.toString())
                        eventFlow.emit(
                            AuthUiEvents.ShowSnackbar(
                                e.message ?: "Something went wrong"
                            )
                        )
                    }
                }
            }
        }
    }
}