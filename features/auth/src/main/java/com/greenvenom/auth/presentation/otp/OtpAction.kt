package com.greenvenom.auth.presentation.otp

sealed interface OtpAction {
    data class OnEnterNumber(val number: Int?, val index: Int): OtpAction
    data class OnChangeFieldFocused(val index: Int): OtpAction
    data object OnKeyboardBack: OtpAction
    data object ResetState: OtpAction
    data object ResetNetworkResult: OtpAction
}