package com.seravian.onboarding.domain

import com.greenvenom.networking.data.Result
import com.greenvenom.networking.domain.NetworkError

interface OnBoardingRepository {
    suspend fun updateUserDetails(
        fullName: String,
        userType: String,
        phoneNumber: String,
        birthDate: String,
        gender: String,
    ): Result<Any, NetworkError>
}