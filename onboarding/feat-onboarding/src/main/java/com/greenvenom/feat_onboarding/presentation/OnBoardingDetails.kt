package com.greenvenom.feat_onboarding.presentation

import com.seravian.core_profile.domain.utils.Gender
import com.seravian.core_profile.domain.utils.Role

data class OnBoardingDetails(
    val fullName: String? = null,
    val dateOfBirth: String? = null,
    val gender: Gender? = null,
    val role: Role? = null
)
