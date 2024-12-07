package com.seravian.data.auth.usecase

import android.util.Patterns
import com.seravian.data.auth.CredentialType
import com.seravian.domain.auth.CredentialState

object ValidateInput {
    fun validateUsername(username: String): CredentialState {
        return when {
            username.isEmpty() -> CredentialState.InValid("Username cannot be empty")
            username.length < 6 -> CredentialState.InValid("Minimum 6 characters long")

            else -> CredentialState.Valid
        }
    }

    fun validateEmail(email: String): CredentialState {
        return when {
            email.isEmpty() -> CredentialState.InValid("Email cannot be empty")
            !Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() -> CredentialState.InValid("Invalid email address")

            else -> CredentialState.Valid
        }
    }

    fun validatePassword(password: String): CredentialState {
        return when {
            password.isEmpty() -> CredentialState.InValid("Password cannot be empty")
            password.length < 8 -> CredentialState.InValid("Minimum 8 characters long")
            !password.matches(".*[0-9].*".toRegex()) -> CredentialState.InValid("Minimum one number")
            !password.matches(".*[A-Z].*".toRegex()) -> CredentialState.InValid("Minimum one uppercase letter")
            !password.matches(".*[a-z].*".toRegex()) -> CredentialState.InValid("Minimum one lowercase letter")
            !password.matches(".*[!@#$%^&*()_+].*".toRegex()) -> CredentialState.InValid("Minimum one special character")

            else -> CredentialState.Valid
        }
    }

    fun validateLoginPassword(password: String): CredentialState {
        return when {
            password.isEmpty() -> CredentialState.InValid("Password cannot be empty")

            else -> CredentialState.Valid
        }
    }

    fun validatePasswordConfirmation(
        password: String,
        confirmPassword: String
    ): CredentialState {
        return when {
            confirmPassword.isEmpty() -> CredentialState.InValid("Confirmation cannot be empty")
            confirmPassword != password -> CredentialState.InValid("Passwords do not match")

            else -> CredentialState.Valid
        }
    }
}