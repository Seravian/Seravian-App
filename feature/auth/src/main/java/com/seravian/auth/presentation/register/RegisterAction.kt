package com.seravian.auth.presentation.register

sealed interface RegisterAction {
    data class ValidateUsername(val username: String): RegisterAction
    data class ValidateEmail(val email: String): RegisterAction
    data class ValidatePassword(val password: String): RegisterAction
    data class ValidatePasswordConfirmation(
        val password: String,
        val confirmPassword: String
    ): RegisterAction
    data class Register(
        val username: String,
        val email: String,
        val password: String,
    ): RegisterAction
    data object ResetRegisterState: RegisterAction
}