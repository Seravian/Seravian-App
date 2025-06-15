package com.seravian.feat_onboarding.domain

import com.seravian.core_network.data.NetworkError
import com.seravian.core_network.data.NetworkResult
import com.seravian.core_onboarding.data.dto.request.OnBoardingRequest
import com.seravian.core_onboarding.data.dto.response.OnBoardingResponse

interface OnBoardingRemoteDataSource {
    suspend fun updateUserDetails(
        onBoardingRequest: OnBoardingRequest
    ): NetworkResult<OnBoardingResponse, NetworkError>
}