package com.greenvenom.validation

import android.util.Patterns
import com.arpitkatiyarprojects.countrypicker.utils.CountryPickerUtils
import com.greenvenom.validation.domain.ValidationError
import com.greenvenom.validation.domain.ValidationResult

object ValidateInput {
    fun validateUsername(username: String): ValidationResult<Unit, ValidationError> {
        return when {
            username.isEmpty() -> ValidationResult.Error(ValidationError.EMPTY_USER_NAME)
            username.length < 6 -> ValidationResult.Error(ValidationError.MINIMUM_6_CHARACTERS)

            else -> ValidationResult.Success(Unit)
        }
    }

    fun validateEmail(email: String): ValidationResult<Unit, ValidationError> {
        return when {
            email.isEmpty() -> ValidationResult.Error(ValidationError.EMPTY_EMAIL)
            !Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() -> ValidationResult.Error(ValidationError.INVALID_EMAIL)

            else -> ValidationResult.Success(Unit)
        }
    }

    fun validatePassword(password: String): ValidationResult<Unit, ValidationError> {
        return when {
            password.isEmpty() -> ValidationResult.Error(ValidationError.EMPTY_PASSWORD)
            password.length < 8 -> ValidationResult.Error(ValidationError.MINIMUM_8_CHARACTERS)
            !password.matches(".*[0-9].*".toRegex()) -> ValidationResult.Error(ValidationError.MINIMUM_1_NUMBER)
            !password.matches(".*[A-Z].*".toRegex()) -> ValidationResult.Error(ValidationError.MINIMUM_1_UPPERCASE_LETTER)
            !password.matches(".*[a-z].*".toRegex()) -> ValidationResult.Error(ValidationError.MINIMUM_1_LOWERCASE_LETTER)
            !password.matches(".*[!@#$%^&*()_+].*".toRegex()) -> ValidationResult.Error(
                ValidationError.MINIMUM_1_SPECIAL_CHARACTER)

            else -> ValidationResult.Success(Unit)
        }
    }

    fun validatePasswordConfirmation(
        password: String,
        confirmPassword: String
    ): ValidationResult<Unit, ValidationError> {
        return when {
            confirmPassword.isEmpty() -> ValidationResult.Error(ValidationError.EMPTY_PASSWORD_CONFIRMATION)
            confirmPassword != password -> ValidationResult.Error(ValidationError.PASSWORDS_MISMATCH)

            else -> ValidationResult.Success(Unit)
        }
    }

    fun validateLoginPassword(password: String): ValidationResult<Unit, ValidationError> {
        return when {
            password.isEmpty() -> ValidationResult.Error(ValidationError.EMPTY_PASSWORD)

            else -> ValidationResult.Success(Unit)
        }
    }

    fun validateFullName(name: String): ValidationResult<Unit, ValidationError> {
        val trimmedName = name.trim()

        return when {
            trimmedName.isEmpty() -> ValidationResult.Error(ValidationError.EMPTY_NAME)

            !trimmedName.all { it.isLetter() || it.isWhitespace() || it in setOf('-', '\'', '.') } ->
                ValidationResult.Error(ValidationError.INVALID_CHARACTERS)

            trimmedName.split(" ").filter { it.isNotBlank() }.size < 2 ->
                ValidationResult.Error(ValidationError.INSUFFICIENT_PARTS)

            else -> ValidationResult.Success(Unit)
        }
    }

    fun validateMobileNumber(mobileNumber: String, countryCode: String): ValidationResult<String, ValidationError> {
        val isValid = CountryPickerUtils.isMobileNumberValid(mobileNumber, countryCode)
        val validationResult = if (isValid) {
            val formattedNumber = CountryPickerUtils.getFormattedMobileNumber(mobileNumber, countryCode)
            ValidationResult.Success(formattedNumber)
        } else {
            ValidationResult.Error(ValidationError.INVALID_PHONE_NUMBER)
        }

        return validationResult
    }
}