package com.seravian.feat_network.data.features.onboarding

import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_onboarding.data.dto.request.OnBoardingRequest
import com.greenvenom.core_onboarding.data.dto.response.OnBoardingResponse
import com.greenvenom.core_onboarding.domain.OnBoardingRepository
import com.seravian.core_local.domain.LocalDataSource
import com.seravian.core_local.domain.LocalTokenDataSource
import com.seravian.feat_network.domain.RemoteDataSource
import com.seravian.feat_network.domain.TokensRepository
import com.seravian.feat_network.util.extractProfile
import com.seravian.feat_network.util.extractTokens

class OnBoardingRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val roomDataSource: LocalDataSource,
    private val localTokenDataSource: LocalTokenDataSource
): OnBoardingRepository {
    override suspend fun updateUserDetails(
        onBoardingRequest: OnBoardingRequest
    ): NetworkResult<OnBoardingResponse, NetworkError> {
        val response = remoteDataSource.updateUserDetails(onBoardingRequest)

        return response.onSuccess {
            roomDataSource.insertProfile(it.extractProfile().toProfileEntity())
            localTokenDataSource.saveTokenLocally(it.extractTokens())
        }
    }
}