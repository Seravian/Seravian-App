package com.seravian.seravianapp.data.features.onboarding

import com.greenvenom.networking.data.NetworkError
import com.greenvenom.networking.data.NetworkResult
import com.seravian.onboarding.domain.OnBoardingRepository

class OnBoardingRepositoryImpl(
    private val remoteDataSource: com.seravian.seravianapp.domain.remote.RemoteDataSource
): OnBoardingRepository {
    override suspend fun updateUserDetails(
        fullName: String,
        userType: String,
        phoneNumber: String,
        birthDate: String,
        gender: String
    ): NetworkResult<Any, NetworkError> {
        return remoteDataSource.updateUserDetails(
            fullName = fullName,
            userType = userType,
            phoneNumber = phoneNumber,
            birthDate = birthDate,
            gender = gender
        )
    }
}