package com.seravian.feat_network.data.features.onboarding

import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_onboarding.data.dto.request.OnBoardingRequest
import com.greenvenom.core_onboarding.data.dto.response.OnBoardingResponse
import com.greenvenom.core_onboarding.domain.OnBoardingRepository
import com.seravian.feat_network.domain.RemoteDataSource

class OnBoardingRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
): OnBoardingRepository {
    override suspend fun updateUserDetails(
        onBoardingRequest: OnBoardingRequest
    ): NetworkResult<OnBoardingResponse, NetworkError> {
        return remoteDataSource.updateUserDetails(onBoardingRequest)
    }
}