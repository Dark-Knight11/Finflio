package com.finflio.feature_authentication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finflio.core.data.model.UserSettings
import com.finflio.core.data.util.SessionManager
import com.finflio.feature_authentication.presentation.utils.AuthEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    fun onEvent(authEvents: AuthEvents) {
        viewModelScope.launch {
            when (authEvents) {
                is AuthEvents.Login -> {
                    println("${authEvents.email}, ${authEvents.password}")
                    sessionManager.saveUserData(UserSettings(authEvents.email, authEvents.password))
                }

                AuthEvents.Register -> {
                    sessionManager.getUserData().collectLatest {
                        println(it)
                    }
                }
            }
        }
    }
}