package com.seravian.seravianapp.features.auth.presentation.register

import androidx.lifecycle.viewModelScope
import com.seravian.data.auth.usecase.ValidateInput
import com.seravian.seravianapp.core.presentation.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(): BaseViewModel() {
    var state = MutableStateFlow(RegisterInputState())
        private set

    fun registerAction(action: RegisterAction) {
        when(action) {
            is RegisterAction.ValidateUsername -> {
                state.value = state.value.copy(
                    usernameValidity = ValidateInput.validateUsername(action.username)
                )
            }
            is RegisterAction.ValidateEmail -> {
                state.value = state.value.copy(
                    emailValidity = ValidateInput.validateEmail(action.email)
                )
            }
            is RegisterAction.ValidatePassword -> {
                state.value = state.value.copy(
                    passwordValidity = ValidateInput.validatePassword(action.password)
                )
            }
            is RegisterAction.ValidatePasswordConfirmation -> {
                state.value = state.value.copy(
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
        showLoading()
        viewModelScope.launch {
            // Network Call Register Suspend Function
        }
    }
}