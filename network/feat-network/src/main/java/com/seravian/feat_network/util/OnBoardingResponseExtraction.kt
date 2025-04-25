package com.seravian.feat_network.util

import com.greenvenom.core_onboarding.data.dto.response.OnBoardingResponse
import com.seravian.core_local.data.TokensInfo
import com.seravian.core_profile.domain.Profile

fun OnBoardingResponse.extractTokens(): TokensInfo {
    return TokensInfo(
        accessToken = this.tokens?.accessToken ?: "",
        accessExpiresIn = this.tokens?.accessTokenExpirationUtc ?: "",
        refreshToken = this.tokens?.refreshToken
    )
}

fun OnBoardingResponse.extractProfile(): Profile {
    return Profile(
        id = this.userId,
        email = this.email,
        fullName = this.fullName,
        dateOfBirth = this.dateOfBirth,
        role = this.role,
        gender = this.gender,
        isEmailVerified = this.isEmailVerified,
        isDoctorVerified = this.isDoctorVerified,
        isProfileSetupComplete = this.isProfileSetupComplete
    )
}