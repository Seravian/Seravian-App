package com.seravian.feat_network.data.local

import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.TokenInfo
import com.greenvenom.core_network.domain.TokenRepository
import com.seravian.feat_network.domain.local.TokenDataSource
import com.seravian.feat_network.domain.remote.RemoteDataSource

class TokenRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val tokenDataSource: TokenDataSource
): TokenRepository {
    override suspend fun getStoredToken(): TokenInfo? {
        return tokenDataSource.getStoredToken()
    }

    override suspend fun refreshToken(
        refreshToken: String
    ): NetworkResult<TokenInfo, NetworkError> {
//        getStoredToken()?.let { storedInfo ->
//            require(storedInfo.accessToken.isNotEmpty())
//            require(storedInfo.refreshToken.isNotEmpty())
//            val newTokenResponse = remoteDataSource.refreshToken(storedInfo.refreshToken)
//            newTokenResponse.onSuccess { newToken ->
//                saveTokenLocally(newToken)
//                return NetworkResult.Success(newToken)
//            }
//        }
        TODO()
    }

    override suspend fun saveTokenLocally(tokenInfo: TokenInfo) {
        tokenDataSource.saveTokenLocally(tokenInfo)
    }

    override suspend fun deleteToken() {
        tokenDataSource.deleteToken()
    }
}