package com.seravian.seravianapp.features.auth.presentation.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.seravian.data.auth.usecase.ValidateInput
import com.seravian.seravianapp.core.presentation.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(): BaseViewModel() {

    val emailState = mutableStateOf("")
    val passwordState = mutableStateOf("")
    val emailErrorState = mutableStateOf("")
    val passwordErrorState = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    private var _state = MutableStateFlow(LoginInputState())
    val state = _state.value

    fun loginAction(action: LoginAction) {
        when(action) {

            is LoginAction.ValidateEmail -> {
                _state.value = _state.value.copy(
                    emailValidity = ValidateInput.validateEmail(action.email)
                )
            }
            is LoginAction.ValidatePassword -> {
                _state.value = _state.value.copy(
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
            // Network Call Login Suspend Function
        }
    }
}
