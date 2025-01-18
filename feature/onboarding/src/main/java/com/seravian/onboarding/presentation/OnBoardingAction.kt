package com.seravian.onboarding.presentation

interface OnBoardingAction {
    data class UpdateAuthUserType(
        val type: String
    ): OnBoardingAction
    data class UpdateAuthUserGender(
        val gender: String
    ): OnBoardingAction
    data class UpdateOnBoardingData(
        val fullName: String,
        val birthDate: String
    ): OnBoardingAction
    data class ValidateFullName(val fullName: String): OnBoardingAction
    data class ValidatePhoneNumber(
        val phoneNumber: String,
        val countryCode: String
    ): OnBoardingAction
    data class NavigateForm(val isForward: Boolean): OnBoardingAction
    data object UploadDetailsAuth: OnBoardingAction
    data object ResetState: OnBoardingAction

}