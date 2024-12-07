package com.seravian.seravianapp.features.auth.presentation.register

import androidx.lifecycle.viewModelScope
import com.seravian.data.auth.usecase.ValidateInput
import com.seravian.seravianapp.core.presentation.BaseViewModel
import com.seravian.seravianapp.features.auth.presentation.login.LoginInputState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(): BaseViewModel() {
    private var _state = MutableStateFlow(RegisterInputState())
    val state = _state.value

    fun onAction(action: RegisterAction) {
        when(action) {
            is RegisterAction.ValidateUsername -> {
                _state.value = _state.value.copy(
                    usernameValidity = ValidateInput.validateUsername(action.username)
                )
            }
            is RegisterAction.ValidateEmail -> {
                _state.value = _state.value.copy(
                    emailValidity = ValidateInput.validateEmail(action.email)
                )
            }
            is RegisterAction.ValidatePassword -> {
                _state.value = _state.value.copy(
                    passwordValidity = ValidateInput.validatePassword(action.password)
                )
            }
            is RegisterAction.ValidatePasswordConfirmation -> {
                _state.value = _state.value.copy(
                    confirmPasswordValidity = ValidateInput.validatePasswordConfirmation(
                        password = action.password,
                        confirmPassword = action.confirmPassword
                    )
                )
            }
            is RegisterAction.Register -> registerAccount(
                username = action.username,
                email = action.email,
                password = action.password,
                confirmPassword = action.confirmPassword
            )
            else -> Unit
        }
    }

    fun registerAccount(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {
            // Network Call Register Suspend Function
        }
    }
}