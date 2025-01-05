package com.seravian.auth.presentation.user_details

interface AuthUserDetailsAction {
    data class UpdateAuthUserType(
        val userType: String
    ): AuthUserDetailsAction
    data class UpdateAuthUserGender(
        val userGender: String
    ): AuthUserDetailsAction
    data class UpdateAuthUserDetails(
        val userFullName: String,
        val userPhoneNumber: String,
        val userBirthDate: String
    ): AuthUserDetailsAction
    data class NavigateForm(val isForward: Boolean): AuthUserDetailsAction
    data object UploadDetailsAuth: AuthUserDetailsAction
    data object ResetState: AuthUserDetailsAction

}