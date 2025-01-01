package com.seravian.auth.presentation.reset_password

interface ResetPasswordAction {
    data class UpdateEmail(val email: String): ResetPasswordAction
    data class ValidatePassword(val password: String): ResetPasswordAction
    data class ValidatePasswordConfirmation(
        val password: String,
        val confirmPassword: String
    ): ResetPasswordAction
    data class ResetPassword(val newPassword: String): ResetPasswordAction
    data object ResetState: ResetPasswordAction
}