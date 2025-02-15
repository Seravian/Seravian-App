package com.greenvenom.auth.presentation.login

import androidx.lifecycle.viewModelScope
import com.greenvenom.auth.domain.repository.AuthRepository
import com.greenvenom.ui.presentation.BaseViewModel
import com.greenvenom.validation.ValidateInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
): BaseViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    fun loginAction(action: LoginAction) {
        when(action) {
            is LoginAction.ValidateEmail -> {
                _loginState.value = _loginState.value.copy(
                    emailValidity = ValidateInput.validateEmail(action.email)
                )
            }
            is LoginAction.ValidatePassword -> {
                _loginState.value = _loginState.value.copy(
                    passwordValidity = ValidateInput.validateLoginPassword(action.password)
                )
            }
            is LoginAction.Login -> loginUser(
                email = action.email,
                password = action.password
            )
            is LoginAction.ResetState -> resetState()
            is LoginAction.ResetNetworkResult -> resetNetworkResult()
        }
    }

    private fun loginUser(
        email: String,
        password: String,
    ) {
        viewModelScope.launch {
            val result = authRepository.loginUser(email, password)
            _loginState.update { it.copy(loginResult = result) }
        }
    }

    private fun resetState() {
        _loginState.update { LoginState() }
    }

    private fun resetNetworkResult() {
        _loginState.update { it.copy(loginResult = null) }
    }
}
