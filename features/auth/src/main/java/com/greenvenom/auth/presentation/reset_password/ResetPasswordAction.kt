package com.greenvenom.auth.presentation.reset_password

interface ResetPasswordAction {
    data class UpdateEmail(val email: String): ResetPasswordAction
    data class ValidatePassword(val password: String): ResetPasswordAction
    data class ValidatePasswordConfirmation(
        val password: String,
        val confirmPassword: String
    ): ResetPasswordAction
    data class SendResetPasswordEmail(val email: String): ResetPasswordAction
    data class UpdatePassword(val newPassword: String): ResetPasswordAction
    data object ResetState: ResetPasswordAction
    data object ResetEmailResult: ResetPasswordAction
    data object ResetPasswordResult: ResetPasswordAction
}