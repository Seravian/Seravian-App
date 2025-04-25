package com.greenvenom.feat_tokens.data

import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.map
import com.greenvenom.core_network.domain.repository.RemoteDataSource
import com.greenvenom.core_tokens.data.dto.request.RefreshTokenRequest
import com.greenvenom.core_tokens.domain.Tokens
import com.greenvenom.core_tokens.domain.repo.TokenDataSource
import com.greenvenom.core_network.domain.repository.TokensRepository

class TokensRepositoryImpl(
    private val tokenDataSource: TokenDataSource,
    private val remoteDataSource: RemoteDataSource
): TokensRepository {
    override suspend fun getStoredTokens(): Tokens? {
        return tokenDataSource.getStoredTokens()
    }

    override suspend fun refreshToken(
        refreshTokenRequest: RefreshTokenRequest
    ): NetworkResult<Tokens, NetworkError> {
        return remoteDataSource.refreshToken(refreshTokenRequest).map { it.extractTokens() }
    }

    override suspend fun saveTokensLocally(tokens: Tokens) {
        tokenDataSource.saveTokensLocally(tokens)
    }

    override suspend fun deleteTokens() {
        tokenDataSource.deleteTokens()
    }
}