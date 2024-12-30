package com.seravian.auth.presentation.otp.viewmodel

import androidx.lifecycle.viewModelScope
import com.seravian.auth.data.AuthError
import com.seravian.auth.data.repository.AuthStateRepository
import com.seravian.auth.domain.repository.OtpRepository
import com.seravian.auth.presentation.otp.OtpAction
import com.seravian.auth.presentation.otp.OtpState
import com.seravian.domain.network.Result
import com.seravian.domain.network.onError
import com.seravian.ui.presentation.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OtpViewModel(
    private val authStateRepository: AuthStateRepository,
    private val otpRepository: OtpRepository
): BaseViewModel() {
    var otpState = MutableStateFlow(OtpState())
        private set

    private val _otpExceptionHandler = CoroutineExceptionHandler { _, exception ->
        otpState.update { otpState.value.copy(
            otpResult = Result.Error(AuthError(exception.message.toString())),
        )}
        hideLoading()
        otpState.value.otpResult?.onError { showErrorMessage(it.message) }
    }

    fun otpAction(action: OtpAction) {
        when(action) {
            is OtpAction.OnChangeFieldFocused -> {
                otpState.update { it.copy(
                    focusedIndex = action.index
                ) }
            }
            is OtpAction.OnEnterNumber -> {
                enterNumber(action.number, action.index)
            }

            is OtpAction.OnKeyboardBack -> {
                val previousIndex = getPreviousFocusedIndex(otpState.value.focusedIndex)
                otpState.update { it.copy(
                    code = it.code.mapIndexed { index, number ->
                        if(index == previousIndex) {
                            null
                        } else {
                            number
                        }
                    },
                    focusedIndex = previousIndex
                ) }
            }
            is OtpAction.OnSuccess -> {
                resetOtpState()
            }
        }
    }

    private fun verifyOtp(email: String, otp: String) {
        viewModelScope.launch(_otpExceptionHandler) {
            otpRepository.verifyOtp(email, otp)
        }.invokeOnCompletion { otpState.update { it.copy(otpResult = Result.Success(Unit)) } }
    }

    private fun enterNumber(number: Int?, index: Int) {
        val newCode = otpState.value.code.mapIndexed { currentIndex, currentNumber ->
            if(currentIndex == index) {
                number
            } else {
                currentNumber
            }
        }
        val wasNumberRemoved = number == null
        otpState.update { it.copy(
            code = newCode,
            focusedIndex = if(wasNumberRemoved || it.code.getOrNull(index) != null) {
                it.focusedIndex
            } else {
                getNextFocusedTextFieldIndex(
                    currentCode = it.code,
                    currentFocusedIndex = it.focusedIndex
                )
            },
        ) }
        if (newCode.all { it != null }) {
            val otp = newCode.joinToString("") { it.toString() }
            authStateRepository.authState.value.email?.let { verifyOtp(it, otp) }
        }
    }

    private fun getPreviousFocusedIndex(currentIndex: Int?): Int? {
        return currentIndex?.minus(1)?.coerceAtLeast(0)
    }

    private fun getNextFocusedTextFieldIndex(
        currentCode: List<Int?>,
        currentFocusedIndex: Int?
    ): Int? {
        if(currentFocusedIndex == null) {
            return null
        }

        if(currentFocusedIndex == 3) {
            return currentFocusedIndex
        }

        return getFirstEmptyFieldIndexAfterFocusedIndex(
            code = currentCode,
            currentFocusedIndex = currentFocusedIndex
        )
    }

    private fun getFirstEmptyFieldIndexAfterFocusedIndex(
        code: List<Int?>,
        currentFocusedIndex: Int
    ): Int {
        code.forEachIndexed { index, number ->
            if(index <= currentFocusedIndex) {
                return@forEachIndexed
            }
            if(number == null) {
                return index
            }
        }
        return currentFocusedIndex
    }

    private fun resetOtpState() {
        otpState.update { OtpState() }
    }
}