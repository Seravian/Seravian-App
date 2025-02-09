package com.seravian.onboarding.data

import com.greenvenom.networking.data.Result
import com.greenvenom.networking.domain.NetworkError
import com.greenvenom.networking.domain.datasource.RemoteDataSource
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
    ): Result<Any, NetworkError> {
        return remoteDataSource.updateUserDetails(
            userType = userType,
            phoneNumber = phoneNumber,
            birthDate = birthDate,
            gender = gender
        )
    }
}