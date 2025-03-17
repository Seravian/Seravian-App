package com.greenvenom.core_network.domain

import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.TokenInfo

interface TokenRepository {
    suspend fun getStoredToken(): TokenInfo?
    suspend fun refreshToken(refreshToken: String): NetworkResult<TokenInfo, NetworkError>
    suspend fun saveTokenLocally(tokenInfo: TokenInfo)
    suspend fun deleteToken()
}