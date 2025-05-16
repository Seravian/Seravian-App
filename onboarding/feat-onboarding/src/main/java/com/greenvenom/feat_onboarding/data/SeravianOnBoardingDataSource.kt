package com.greenvenom.feat_onboarding.data

import com.greenvenom.core_network.api.utils.constructUrl
import com.greenvenom.core_network.api.utils.safeCall
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_onboarding.data.dto.request.OnBoardingRequest
import com.greenvenom.core_onboarding.data.dto.response.OnBoardingResponse
import com.greenvenom.feat_onboarding.domain.OnBoardingRemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class SeravianOnBoardingDataSource(
    private val authorizedHttpClient: HttpClient
): OnBoardingRemoteDataSource {
    override suspend fun updateUserDetails(
        onBoardingRequest: OnBoardingRequest
    ): NetworkResult<OnBoardingResponse, NetworkError> {
        return safeCall<OnBoardingResponse> {
            authorizedHttpClient.post(constructUrl("auth/complete-profile-setup")) {
                setBody(onBoardingRequest)
            }
        }.onSuccess { authorizedHttpClient.authProvider<BearerAuthProvider>()?.clearToken() }
    }
}