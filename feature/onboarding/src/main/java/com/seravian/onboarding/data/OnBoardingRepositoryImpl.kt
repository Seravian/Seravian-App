package com.seravian.onboarding.data

import com.seravian.domain.datasource.RemoteDataSource
import com.seravian.onboarding.domain.OnBoardingRepository

class OnBoardingRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
): OnBoardingRepository {
    override suspend fun updateUserDetails(
        fullName: String,
        userType: String,
        phoneNumber: String,
        birthDate: String,
        gender: String
    ) {
        remoteDataSource.updateUserDetails(
            fullName = fullName,
            userType = userType,
            phoneNumber = phoneNumber,
            birthDate = birthDate,
            gender = gender
        )
    }
}