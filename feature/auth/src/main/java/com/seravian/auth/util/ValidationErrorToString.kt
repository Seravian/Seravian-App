package com.seravian.auth.util

import android.content.Context
import com.seravian.auth.R

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
    }
    return context.getString(resId)
}