package com.seravian.auth.presentation.login

import androidx.lifecycle.viewModelScope
import com.seravian.data.errors.AuthError
import com.seravian.auth.domain.repository.LoginRepository
import com.greenvenom.validation.ValidateInput
import com.greenvenom.networking.data.Result
import com.greenvenom.networking.data.onError
import com.seravian.ui.presentation.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepository
): BaseViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _loginExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _loginState.value = _loginState.value.copy(
            loginResult = Result.Error(AuthError(exception.message.toString())),
        )
        hideLoading()
        _loginState.value.loginResult?.onError { showErrorMessage(it.message) }
    }

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
        }
    }

    private fun loginUser(
        email: String,
        password: String,
    ) {
        showLoading()
        viewModelScope.launch(_loginExceptionHandler) {
            loginRepository.loginUser(email, password)
        }.invokeOnCompletion {
            _loginState.update { it.copy(loginResult = Result.Success(Unit)) }
            hideLoading()
        }
    }

    private fun resetState() {
        _loginState.value = LoginState()
    }
}
