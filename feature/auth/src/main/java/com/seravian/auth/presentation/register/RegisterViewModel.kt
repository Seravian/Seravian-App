package com.seravian.auth.presentation.register

import androidx.lifecycle.viewModelScope
import com.seravian.auth.data.AuthError
import com.seravian.auth.domain.repository.RegisterRepository
import com.seravian.auth.util.ValidateInput
import com.seravian.domain.network.Result
import com.seravian.domain.network.onError
import com.seravian.ui.presentation.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerRepository: RegisterRepository
): BaseViewModel() {
    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    private val _registerExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _registerState.value = _registerState.value.copy(
            registrationResult = Result.Error(AuthError(exception.message.toString())),
        )
        hideLoading()
        _registerState.value.registrationResult?.onError { showErrorMessage(it.message) }
    }

    fun registerAction(action: RegisterAction) {
        when(action) {
            is RegisterAction.ValidateUsername -> {
                _registerState.value = _registerState.value.copy(
                    usernameValidity = ValidateInput.validateUsername(action.username)
                )
            }
            is RegisterAction.ValidateEmail -> {
                _registerState.value = _registerState.value.copy(
                    emailValidity = ValidateInput.validateEmail(action.email)
                )
            }
            is RegisterAction.ValidatePassword -> {
                _registerState.value = _registerState.value.copy(
                    passwordValidity = ValidateInput.validatePassword(action.password)
                )
            }
            is RegisterAction.ValidatePasswordConfirmation -> {
                _registerState.value = _registerState.value.copy(
                    confirmPasswordValidity = ValidateInput.validatePasswordConfirmation(
                        password = action.password,
                        confirmPassword = action.confirmPassword
                    )
                )
            }
            is RegisterAction.Register -> registerUser(
                username = action.username,
                email = action.email,
                password = action.password,
            )
            is RegisterAction.ResetState -> resetState()
        }
    }

    private fun registerUser(
        username: String,
        email: String,
        password: String,
    ) {
        showLoading()
        viewModelScope.launch(_registerExceptionHandler) {
            registerRepository.registerUser(username, email, password)
        }.invokeOnCompletion {
            _registerState.update { it.copy(registrationResult = Result.Success(Unit)) }
            hideLoading()
        }
    }

    private fun resetState() {
        _registerState.value = RegisterState()
    }
}