package com.seravian.auth.presentation.user_details

interface AuthUserDetailsAction {
    data class UpdateAuthUserType(
        val type: String
    ): AuthUserDetailsAction
    data class UpdateAuthUserGender(
        val gender: String
    ): AuthUserDetailsAction
    data class UpdateAuthUserDetails(
        val fullName: String,
        val birthDate: String
    ): AuthUserDetailsAction
    data class ValidateFullName(val fullName: String): AuthUserDetailsAction
    data class ValidatePhoneNumber(
        val phoneNumber: String,
        val countryCode: String
    ): AuthUserDetailsAction
    data class NavigateForm(val isForward: Boolean): AuthUserDetailsAction
    data object UploadDetailsAuth: AuthUserDetailsAction
    data object ResetState: AuthUserDetailsAction

}