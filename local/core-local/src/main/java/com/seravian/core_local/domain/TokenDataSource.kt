package com.seravian.core_local.domain

import com.seravian.core_local.data.TokensInfo

interface TokenDataSource {
    suspend fun getStoredToken(): TokensInfo?
    suspend fun saveTokenLocally(tokensInfo: TokensInfo)
    suspend fun deleteToken()
}