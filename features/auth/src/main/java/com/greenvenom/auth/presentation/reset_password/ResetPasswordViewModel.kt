package com.greenvenom.auth.presentation.reset_password

import androidx.lifecycle.viewModelScope
import com.greenvenom.auth.data.repository.EmailStateRepository
import com.greenvenom.auth.domain.repository.AuthRepository
import com.greenvenom.ui.presentation.BaseViewModel
import com.greenvenom.validation.ValidateInput
import com.greenvenom.validation.domain.onError
import com.greenvenom.validation.domain.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val emailStateRepository: EmailStateRepository,
    private val authRepository: AuthRepository
): BaseViewModel() {
    private val _resetPasswordState = MutableStateFlow(ResetPasswordState())
    val resetPasswordState = _resetPasswordState.asStateFlow()

    fun resetPasswordAction(action: ResetPasswordAction) {
        when (action) {
            is ResetPasswordAction.UpdateEmail -> {
                validateEmail(action.email)
            }
            is ResetPasswordAction.ValidatePassword -> {
                _resetPasswordState.value = _resetPasswordState.value.copy(
                    passwordValidity = ValidateInput.validatePassword(action.password)
                )
            }
            is ResetPasswordAction.ValidatePasswordConfirmation -> {
                _resetPasswordState.value = _resetPasswordState.value.copy(
                    confirmPasswordValidity = ValidateInput.validatePasswordConfirmation(
                        password = action.password,
                        confirmPassword = action.confirmPassword
                    )
                )
            }
            is ResetPasswordAction.SendResetPasswordEmail -> {
                emailStateRepository.emailState.value.email?.let {
                    sendPasswordResetEmail(
                        email = it
                    )
                }
            }
            is ResetPasswordAction.UpdatePassword -> {
                updatePassword(action.newPassword)
            }
            is ResetPasswordAction.ResetState -> resetState()
            is ResetPasswordAction.ResetEmailResult -> resetEmailResult()
            is ResetPasswordAction.ResetPasswordResult -> resetPasswordResult()
        }
    }

    private fun validateEmail(email: String) {
        val typedEmailValidity = ValidateInput.validateEmail(email)
        typedEmailValidity.onError { emailStateRepository.updateEmailValidity(typedEmailValidity) }
        typedEmailValidity.onSuccess {
            emailStateRepository.updateEmail(email)
            emailStateRepository.updateEmailValidity(typedEmailValidity)
        }
    }

    private fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            val result = authRepository.sendResetPasswordEmail(email)
            _resetPasswordState.update { it.copy(emailSentNetworkResult = result) }
        }
    }

    private fun updatePassword(newPassword: String) {
        viewModelScope.launch {
            val result = authRepository.updatePassword(newPassword)
            _resetPasswordState.update { it.copy(passwordUpdatedNetworkResult = result) }
        }
    }

    private fun resetState() {
        _resetPasswordState.update { ResetPasswordState() }
    }

    private fun resetEmailResult() {
        _resetPasswordState.update { it.copy(emailSentNetworkResult = null) }
    }

    private fun resetPasswordResult() {
        _resetPasswordState.update { it.copy(passwordUpdatedNetworkResult = null) }
    }
}