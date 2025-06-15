package com.seravian.core_tokens.domain.repo

import com.seravian.core_network.data.EmptyResult
import com.seravian.core_network.data.NetworkError
import com.seravian.core_tokens.data.dto.request.RefreshTokenRequest
import com.seravian.core_tokens.domain.Tokens
import kotlinx.coroutines.flow.Flow

interface TokensDataSource {
    suspend fun refreshTokens(refreshTokenRequest: RefreshTokenRequest): EmptyResult<NetworkError>
    suspend fun getStoredTokens(): Tokens
    fun getStoredTokensFlow(): Flow<Tokens>
    suspend fun saveTokensLocally(tokens: Tokens)
    suspend fun deleteTokens()
}