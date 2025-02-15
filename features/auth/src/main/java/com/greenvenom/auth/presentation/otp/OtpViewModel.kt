package com.greenvenom.auth.presentation.otp

import androidx.lifecycle.viewModelScope
import com.greenvenom.auth.data.repository.EmailStateRepository
import com.greenvenom.auth.domain.repository.AuthRepository
import com.greenvenom.ui.presentation.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OtpViewModel(
    private val emailStateRepository: EmailStateRepository,
    private val authRepository: AuthRepository
): BaseViewModel() {
    private val _otpState = MutableStateFlow(OtpState())
    val otpState = _otpState.asStateFlow()

    fun otpAction(action: OtpAction) {
        when(action) {
            is OtpAction.OnChangeFieldFocused -> {
                _otpState.update { it.copy(
                    focusedIndex = action.index
                ) }
            }
            is OtpAction.OnEnterNumber -> {
                enterNumber(action.number, action.index)
            }

            is OtpAction.OnKeyboardBack -> {
                val previousIndex = getPreviousFocusedIndex(_otpState.value.focusedIndex)
                _otpState.update {
                    it.copy(
                        code = it.code.mapIndexed { index, number ->
                            if(index == previousIndex) {
                                null
                            } else {
                                number
                            }
                        },
                        focusedIndex = previousIndex
                    )
                }
            }
            is OtpAction.ResetState -> resetOtpState()
            is OtpAction.ResetNetworkResult -> resetNetworkResult()
        }
    }

    private fun verifyOtp(email: String, otp: String) {
        showLoading()
        viewModelScope.launch {
            val result = authRepository.verifyOtp(email, otp)
            _otpState.update { it.copy(otpResult = result) }
        }
    }

    private fun enterNumber(number: Int?, index: Int) {
        val newCode = _otpState.value.code.mapIndexed { currentIndex, currentNumber ->
            if(currentIndex == index) {
                number
            } else {
                currentNumber
            }
        }
        val wasNumberRemoved = number == null
        _otpState.update { it.copy(
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
            emailStateRepository.emailState.value.email?.let { verifyOtp(it, otp) }
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

        if(currentFocusedIndex == 5) {
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
        _otpState.update { OtpState() }
        emailStateRepository.resetEmailState()
    }

    private fun resetNetworkResult() {
        _otpState.update { it.copy(otpResult = null) }
    }
}