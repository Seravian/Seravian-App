package com.greenvenom.feat_onboarding.data

import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_onboarding.data.dto.request.OnBoardingRequest
import com.greenvenom.core_onboarding.data.dto.response.OnBoardingResponse
import com.greenvenom.feat_onboarding.domain.OnBoardingRepository
import com.seravian.core_local.domain.LocalDataSource
import com.greenvenom.core_network.domain.repository.RemoteDataSource
import com.greenvenom.core_network.domain.repository.TokensRepository
import com.greenvenom.core_tokens.domain.Tokens

class OnBoardingRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val roomDataSource: LocalDataSource,
    private val tokensRepository: TokensRepository,
): OnBoardingRepository {
    override suspend fun updateUserDetails(
        onBoardingRequest: OnBoardingRequest
    ): NetworkResult<OnBoardingResponse, NetworkError> {
        val response = remoteDataSource.updateUserDetails(onBoardingRequest)

        return response.onSuccess {
            roomDataSource.insertProfile(it.extractProfile().toProfileEntity())
            tokensRepository.saveTokensLocally(it.tokens?.extractTokens() as Tokens)
        }
    }
}