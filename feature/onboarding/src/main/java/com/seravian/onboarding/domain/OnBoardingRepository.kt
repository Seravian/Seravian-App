package com.seravian.onboarding.domain

interface OnBoardingRepository {
    suspend fun updateUserDetails(
        fullName: String,
        userType: String,
        phoneNumber: String,
        birthDate: String,
        gender: String,
    )
}