package com.seravian.auth.presentation.login

import androidx.lifecycle.viewModelScope
import com.seravian.auth.data.AuthError
import com.seravian.auth.util.ValidateInput
import com.seravian.domain.network.util.Result
import com.seravian.domain.network.util.onError
import com.seravian.ui.presentation.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(): BaseViewModel() {

    var loginState = MutableStateFlow(LoginInputState())
        private set

    private val _loginExceptionHandler = CoroutineExceptionHandler { _, exception ->
        loginState.value = loginState.value.copy(
            loginResult = Result.Error(AuthError(exception.message.toString())),
        )
        hideLoading()
        loginState.value.loginResult?.onError { showErrorMessage(it.message) }
    }

    fun loginAction(action: LoginAction) {
        when(action) {
            is LoginAction.ValidateEmail -> {
                loginState.value = loginState.value.copy(
                    emailValidity = ValidateInput.validateEmail(action.email)
                )
            }
            is LoginAction.ValidatePassword -> {
                loginState.value = loginState.value.copy(
                    passwordValidity = ValidateInput.validateLoginPassword(action.password)
                )
            }
            is LoginAction.Login -> loginUser(
                email = action.email,
                password = action.password
            )
            is LoginAction.ResetLoginState -> resetState()
            else -> Unit
        }
    }

    private fun loginUser(
        email: String,
        password: String,
    ) {
        showLoading()

        viewModelScope.launch(_loginExceptionHandler) {
            // Network Call
        }.invokeOnCompletion { hideLoading() }
    }

    private fun resetState() {
        loginState.value = LoginInputState()
    }
}
