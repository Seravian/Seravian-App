package com.greenvenom.core_tokens.domain.repo

import com.greenvenom.core_tokens.domain.Tokens
import kotlinx.coroutines.flow.Flow

interface TokenDataSource {
    suspend fun getStoredTokens(): Tokens
    fun getStoredTokensFlow(): Flow<Tokens>
    suspend fun saveTokensLocally(tokens: Tokens)
    suspend fun deleteTokens()
}