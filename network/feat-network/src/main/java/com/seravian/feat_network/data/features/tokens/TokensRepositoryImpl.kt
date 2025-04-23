package com.seravian.feat_network.data.features.tokens

import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_local.data.TokensInfo
import com.seravian.core_local.domain.TokenDataSource
import com.seravian.feat_network.domain.TokensRepository
import com.seravian.feat_network.domain.RemoteDataSource

class TokensRepositoryImpl(
    private val tokenDataSource: TokenDataSource,
    private val remoteDataSource: RemoteDataSource
): TokensRepository {
    override suspend fun getStoredTokens(): TokensInfo? {
        return tokenDataSource.getStoredToken()
    }

    override suspend fun refreshToken(refreshToken: String): NetworkResult<TokensInfo, NetworkError> {
        return remoteDataSource.refreshToken(refreshToken)
    }

    override suspend fun saveTokensLocally(tokensInfo: TokensInfo) {
        tokenDataSource.saveTokenLocally(tokensInfo)
    }

    override suspend fun deleteTokens() {
        tokenDataSource.deleteToken()
    }
}