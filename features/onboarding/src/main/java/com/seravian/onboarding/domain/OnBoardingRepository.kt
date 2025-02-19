package com.seravian.onboarding.domain

import com.greenvenom.networking.data.NetworkError
import com.greenvenom.networking.data.NetworkResult


interface OnBoardingRepository {
    suspend fun updateUserDetails(
        fullName: String,
        userType: String,
        phoneNumber: String,
        birthDate: String,
        gender: String,
    ): NetworkResult<Any, NetworkError>
}