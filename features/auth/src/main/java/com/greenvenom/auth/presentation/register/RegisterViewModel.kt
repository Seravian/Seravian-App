package com.greenvenom.auth.presentation.register

import androidx.lifecycle.viewModelScope
import com.greenvenom.auth.data.repository.EmailStateRepository
import com.greenvenom.auth.domain.repository.RegisterRepository
import com.greenvenom.networking.data.onSuccess
import com.greenvenom.ui.presentation.BaseViewModel
import com.greenvenom.validation.ValidateInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val emailStateRepository: EmailStateRepository,
    private val registerRepository: RegisterRepository
): BaseViewModel() {
    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

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
            is RegisterAction.ResetNetworkResult -> resetNetworkResult()
        }
    }

    private fun registerUser(
        username: String,
        email: String,
        password: String,
    ) {
        viewModelScope.launch {
            val result = registerRepository.registerUser(username, email, password)
            _registerState.update { it.copy(registrationResult = result) }
            result.onSuccess { emailStateRepository.updateEmail(email) }
        }
    }

    private fun resetState() {
        _registerState.update { RegisterState() }
    }

    private fun resetNetworkResult() {
        _registerState.update { it.copy(registrationResult = null) }
    }
}