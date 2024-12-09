package com.seravian.seravianapp.features.auth.presentation.login

import androidx.lifecycle.viewModelScope
import com.seravian.data.auth.usecase.ValidateInput
import com.seravian.seravianapp.core.presentation.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(): BaseViewModel() {

     var state = MutableStateFlow(LoginInputState())
        private set

    fun loginAction(action: LoginAction) {
        when(action) {
            is LoginAction.ValidateEmail -> {
                state.value = state.value.copy(
                    emailValidity = ValidateInput.validateEmail(action.email)
                )
            }
            is LoginAction.ValidatePassword -> {
                state.value = state.value.copy(
                    passwordValidity = ValidateInput.validateLoginPassword(action.password)
                )
            }
            is LoginAction.Login -> loginAccount(
                email = action.email,
                password = action.password
            )
            else -> Unit
        }
    }

    fun loginAccount(
        email: String,
        password: String,
    ) {
        viewModelScope.launch {
            showLoading()
            // Network Call Login Suspend Function
        }
    }
}
