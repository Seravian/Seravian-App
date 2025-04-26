package com.greenvenom.feat_onboarding.presentation

interface OnBoardingAction {
    data class UpdateAuthUserType(
        val type: Int
    ): OnBoardingAction
    data class UpdateAuthUserGender(
        val gender: Int
    ): OnBoardingAction
    data class UpdateOnBoardingData(
        val fullName: String,
        val birthDate: String
    ): OnBoardingAction
    data class ValidateFullName(val fullName: String): OnBoardingAction
    data class NavigateForm(val isForward: Boolean): OnBoardingAction
    data object UploadDetailsAuth: OnBoardingAction
    data object ResetState: OnBoardingAction
    data object ResetNetworkResult: OnBoardingAction
}