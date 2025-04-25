package com.greenvenom.core_tokens.domain.repo

import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_tokens.data.dto.request.RefreshTokenRequest
import com.greenvenom.core_tokens.domain.Tokens

interface TokensRepository {
    suspend fun getStoredTokens(): Tokens?
    suspend fun refreshToken(
        refreshTokenRequest: RefreshTokenRequest
    ): NetworkResult<Tokens, NetworkError>
    suspend fun saveTokensLocally(tokens: Tokens)
    suspend fun deleteTokens()
}