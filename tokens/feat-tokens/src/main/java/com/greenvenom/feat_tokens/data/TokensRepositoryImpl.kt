package com.greenvenom.feat_tokens.data

import com.greenvenom.core_network.api.utils.constructUrl
import com.greenvenom.core_network.api.utils.safeCall
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.map
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_network.domain.repository.RemoteDataSource
import com.greenvenom.core_tokens.data.dto.request.RefreshTokenRequest
import com.greenvenom.core_tokens.domain.Tokens
import com.greenvenom.core_tokens.domain.repo.TokenDataSource
import com.greenvenom.core_network.domain.repository.TokensRepository
import com.greenvenom.core_tokens.data.dto.response.TokensResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class TokensRepositoryImpl(
    private val tokenDataSource: TokenDataSource,
    private val publicHttpClient: HttpClient
): TokensRepository {
    override suspend fun getStoredTokens(): Tokens? {
        return tokenDataSource.getStoredTokens()
    }

    override suspend fun refreshToken(
        refreshTokenRequest: RefreshTokenRequest
    ): NetworkResult<Tokens, NetworkError> {
        return safeCall<TokensResponse> {
            publicHttpClient.post(urlString = constructUrl("auth/refresh-token")) {
                setBody(refreshTokenRequest)
            }
        }.map { it.extractTokens() }
    }

    override suspend fun saveTokensLocally(tokens: Tokens) {
        tokenDataSource.saveTokensLocally(tokens)
    }

    override suspend fun deleteTokens() {
        tokenDataSource.deleteTokens()
    }
}