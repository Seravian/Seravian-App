package com.seravian.auth.util

import android.util.Patterns
import com.seravian.domain.network.util.Result

object ValidateInput {
    fun validateUsername(username: String): Result<Unit, ValidationError> {
        return when {
            username.isEmpty() -> Result.Error(ValidationError.EMPTY_USER_NAME)
            username.length < 6 -> Result.Error(ValidationError.MINIMUM_6_CHARACTERS)

            else -> Result.Success(Unit)
        }
    }

    fun validateEmail(email: String): Result<Unit, ValidationError> {
        return when {
            email.isEmpty() -> Result.Error(ValidationError.EMPTY_EMAIL)
            !Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() -> Result.Error(ValidationError.INVALID_EMAIL)

            else -> Result.Success(Unit)
        }
    }

    fun validatePassword(password: String): Result<Unit, ValidationError> {
        return when {
            password.isEmpty() -> Result.Error(ValidationError.EMPTY_PASSWORD)
            password.length < 8 -> Result.Error(ValidationError.MINIMUM_8_CHARACTERS)
            !password.matches(".*[0-9].*".toRegex()) -> Result.Error(ValidationError.MINIMUM_1_NUMBER)
            !password.matches(".*[A-Z].*".toRegex()) -> Result.Error(ValidationError.MINIMUM_1_UPPERCASE_LETTER)
            !password.matches(".*[a-z].*".toRegex()) -> Result.Error(ValidationError.MINIMUM_1_LOWERCASE_LETTER)
            !password.matches(".*[!@#$%^&*()_+].*".toRegex()) -> Result.Error(ValidationError.MINIMUM_1_SPECIAL_CHARACTER)

            else -> Result.Success(Unit)
        }
    }

    fun validatePasswordConfirmation(
        password: String,
        confirmPassword: String
    ): Result<Unit, ValidationError> {
        return when {
            confirmPassword.isEmpty() -> Result.Error(ValidationError.EMPTY_PASSWORD_CONFIRMATION)
            confirmPassword != password -> Result.Error(ValidationError.PASSWORDS_MISMATCH)

            else -> Result.Success(Unit)
        }
    }

    fun validateLoginPassword(password: String): Result<Unit, ValidationError> {
        return when {
            password.isEmpty() -> Result.Error(ValidationError.EMPTY_PASSWORD)

            else -> Result.Success(Unit)
        }
    }
}