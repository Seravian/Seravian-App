package com.greenvenom.core_tokens.domain.repo

import com.greenvenom.core_tokens.domain.Tokens

interface TokenDataSource {
    suspend fun getStoredTokens(): Tokens?
    suspend fun saveTokensLocally(tokens: Tokens)
    suspend fun deleteTokens()
}