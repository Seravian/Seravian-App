package com.seravian.feat_network.data.features.onboarding

import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_onboarding.domain.OnBoardingRepository
import com.seravian.feat_network.domain.remote.RemoteDataSource

class OnBoardingRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
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