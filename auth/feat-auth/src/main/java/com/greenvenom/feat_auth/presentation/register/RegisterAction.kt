package com.greenvenom.feat_auth.presentation.register

sealed interface RegisterAction {
    data class ValidateEmail(val email: String): RegisterAction
    data class ValidatePassword(val password: String): RegisterAction
    data class ValidatePasswordConfirmation(
        val password: String,
        val confirmPassword: String
    ): RegisterAction
    data class Register(
        val email: String,
        val password: String,
    ): RegisterAction
    data object ResetState: RegisterAction
    data object ResetNetworkResult: RegisterAction
}