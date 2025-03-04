package com.greenvenom.core_onboarding.domain

import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult


interface OnBoardingRepository {
    suspend fun updateUserDetails(
        fullName: String,
        userType: String,
        phoneNumber: String,
        birthDate: String,
        gender: String,
    ): NetworkResult<Any, NetworkError>
}