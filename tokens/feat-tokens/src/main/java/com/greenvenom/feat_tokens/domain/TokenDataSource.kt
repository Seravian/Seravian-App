package com.greenvenom.feat_tokens.domain

import com.greenvenom.core_tokens.domain.Tokens

interface TokenDataSource {
    suspend fun getStoredTokens(): Tokens?
    suspend fun saveTokensLocally(tokens: Tokens)
    suspend fun deleteTokens()
}