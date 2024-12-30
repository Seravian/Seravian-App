package com.seravian.auth.presentation.reset_password

import com.seravian.auth.presentation.register.RegisterAction

interface ResetPasswordAction {
    data class UpdateEmail(val email: String): ResetPasswordAction
    data class ValidatePassword(val password: String): ResetPasswordAction
    data class ValidatePasswordConfirmation(
        val password: String,
        val confirmPassword: String
    ): ResetPasswordAction
    data class ResetPassword(val newPassword: String): ResetPasswordAction
}