package com.seravian.auth.presentation.reset_password

import androidx.lifecycle.viewModelScope
import com.seravian.auth.data.AuthError
import com.seravian.auth.data.repository.AuthStateRepository
import com.seravian.auth.domain.repository.ResetPasswordRepository
import com.seravian.auth.presentation.login.LoginInputState
import com.seravian.auth.presentation.register.RegisterAction
import com.seravian.auth.util.ValidateInput
import com.seravian.domain.network.Result
import com.seravian.domain.network.onError
import com.seravian.domain.network.onSuccess
import com.seravian.ui.presentation.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val authStateRepository: AuthStateRepository,
    private val resetPasswordRepository: ResetPasswordRepository
): BaseViewModel() {
    var resetPasswordState = MutableStateFlow(ResetPasswordState())
        private set

    private val _resetPasswordExceptionHandler = CoroutineExceptionHandler { _, exception ->
        resetPasswordState.value = resetPasswordState.value.copy(
            resetPasswordResult = Result.Error(AuthError(exception.message.toString())),
        )
        hideLoading()
        resetPasswordState.value.resetPasswordResult?.onError { showErrorMessage(it.message) }
    }

    fun resetPasswordAction(action: ResetPasswordAction) {
        when (action) {
            is ResetPasswordAction.UpdateEmail -> {
                validateEmail(action.email)
            }
            is ResetPasswordAction.ValidatePassword -> {
                resetPasswordState.value = resetPasswordState.value.copy(
                    passwordValidity = ValidateInput.validatePassword(action.password)
                )
            }
            is ResetPasswordAction.ValidatePasswordConfirmation -> {
                resetPasswordState.value = resetPasswordState.value.copy(
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
        }.invokeOnCompletion { hideLoading() }
    }
}