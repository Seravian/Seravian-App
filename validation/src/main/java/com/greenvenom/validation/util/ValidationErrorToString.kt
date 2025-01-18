package com.greenvenom.validation.util

import android.content.Context
import com.greenvenom.validation.R
import com.greenvenom.validation.domain.ValidationError

fun ValidationError.toString(context: Context): String {
    val resId = when (this) {
        ValidationError.EMPTY_USER_NAME -> R.string.empty_user_name_error
        ValidationError.EMPTY_EMAIL -> R.string.empty_email_error
        ValidationError.INVALID_EMAIL -> R.string.invalid_email_error
        ValidationError.EMPTY_PASSWORD -> R.string.empty_password_error
        ValidationError.EMPTY_PASSWORD_CONFIRMATION -> R.string.empty_confirm_password_error
        ValidationError.PASSWORDS_MISMATCH -> R.string.password_mismatch_error
        ValidationError.INVALID_OTP -> R.string.invalid_otp_error
        ValidationError.MINIMUM_6_CHARACTERS -> R.string.minimum_6_characters_error
        ValidationError.MINIMUM_8_CHARACTERS -> R.string.minimum_8_characters_error
        ValidationError.MINIMUM_1_NUMBER -> R.string.at_least_one_number_error
        ValidationError.MINIMUM_1_LOWERCASE_LETTER -> R.string.at_least_one_lowercase_letter_error
        ValidationError.MINIMUM_1_UPPERCASE_LETTER -> R.string.at_least_one_uppercase_letter_error
        ValidationError.MINIMUM_1_SPECIAL_CHARACTER -> R.string.at_least_one_special_character_error
        ValidationError.EMPTY_NAME -> R.string.name_cannot_be_empty
        ValidationError.INVALID_CHARACTERS -> R.string.name_contains_invalid_characters
        ValidationError.INSUFFICIENT_PARTS -> R.string.name_contains_insufficient_parts
        ValidationError.INVALID_PHONE_NUMBER -> R.string.invalid_phone_number
    }
    return context.getString(resId)
}