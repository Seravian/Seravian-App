package com.seravian.feat_network.domain

import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_local.data.TokensInfo

interface TokensRepository {
    suspend fun getStoredTokens(): TokensInfo?
    suspend fun refreshToken(refreshToken: String): NetworkResult<TokensInfo, NetworkError>
    suspend fun saveTokensLocally(tokensInfo: TokensInfo)
    suspend fun deleteTokens()
}