package com.seravian.feat_onboarding.domain.repository

import com.seravian.core_network.data.NetworkError
import com.seravian.core_network.data.NetworkResult
import com.seravian.core_onboarding.data.dto.request.OnBoardingRequest
import com.seravian.core_onboarding.data.dto.response.OnBoardingResponse


interface OnBoardingRepository {
    suspend fun updateUserDetails(
        onBoardingRequest: OnBoardingRequest
    ): NetworkResult<OnBoardingResponse, NetworkError>
}