package com.greenvenom.feat_onboarding.presentation

import com.seravian.core_profile.domain.utils.Gender
import com.seravian.core_profile.domain.utils.Role

interface OnBoardingAction {
    data class UpdateAuthUserType(
        val type: Role
    ): OnBoardingAction
    data class UpdateAuthUserGender(
        val gender: Gender
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