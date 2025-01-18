package com.seravian.auth.presentation.reset_password

import androidx.lifecycle.viewModelScope
import com.seravian.data.AuthError
import com.seravian.auth.data.repository.AuthStateRepository
import com.seravian.auth.domain.repository.ResetPasswordRepository
import com.greenvenom.validation.ValidateInput
import com.greenvenom.validation.domain.onError
import com.greenvenom.validation.domain.onSuccess
import com.seravian.domain.network.Result
import com.seravian.domain.network.onError
import com.seravian.ui.presentation.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val authStateRepository: AuthStateRepository,
    private val resetPasswordRepository: ResetPasswordRepository
): BaseViewModel() {
    private val _resetPasswordState = MutableStateFlow(ResetPasswordState())
    val resetPasswordState = _resetPasswordState.asStateFlow()

    private val _resetPasswordExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _resetPasswordState.value = _resetPasswordState.value.copy(
            resetPasswordResult = Result.Error(AuthError(exception.message.toString())),
        )
        hideLoading()
        _resetPasswordState.value.resetPasswordResult?.onError { showErrorMessage(it.message) }
    }

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
            is ResetPasswordAction.ResetPassword -> {
                authStateRepository.authState.value.email?.let {
                    resetPassword(
                        email = it,
                        newPassword = action.newPassword
                    )
                }
            }
            is ResetPasswordAction.ResetState -> resetState()
        }
    }

    private fun validateEmail(email: String) {
        val typedEmailValidity = ValidateInput.validateEmail(email)
        typedEmailValidity.onError { authStateRepository.updateEmailValidity(typedEmailValidity) }
        typedEmailValidity.onSuccess {
            authStateRepository.updateEmail(email)
            authStateRepository.updateEmailValidity(typedEmailValidity)
        }
    }

    private fun resetPassword(email: String, newPassword: String) {
        showLoading()
        viewModelScope.launch(_resetPasswordExceptionHandler) {
            resetPasswordRepository.resetPassword(email, newPassword)
        }.invokeOnCompletion {
            _resetPasswordState.update { it.copy(resetPasswordResult = Result.Success(Unit)) }
            hideLoading()
        }
    }

    private fun resetState() {
        _resetPasswordState.value = ResetPasswordState()
    }
}