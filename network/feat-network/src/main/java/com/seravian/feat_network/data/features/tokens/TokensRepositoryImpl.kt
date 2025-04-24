package com.seravian.feat_network.data.features.tokens

import com.greenvenom.core_network.api.utils.constructUrl
import com.greenvenom.core_network.api.utils.safeCall
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_local.data.TokensInfo
import com.seravian.core_local.domain.LocalTokenDataSource
import com.seravian.feat_network.domain.TokensRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class TokensRepositoryImpl(
    private val localTokenDataSource: LocalTokenDataSource,
    private val publicHttpClient: HttpClient
): TokensRepository {
    override suspend fun getStoredTokens(): TokensInfo? {
        return localTokenDataSource.getStoredToken()
    }

    override suspend fun refreshToken(refreshToken: String): NetworkResult<TokensInfo, NetworkError> {
        return safeCall {
            publicHttpClient.post(urlString = constructUrl("auth/refresh-token")) {
                setBody(
                    mapOf(
                        "refreshToken" to refreshToken
                    )
                )
            }
        }
    }

    override suspend fun saveTokensLocally(tokensInfo: TokensInfo) {
        localTokenDataSource.saveTokenLocally(tokensInfo)
    }

    override suspend fun deleteTokens() {
        localTokenDataSource.deleteToken()
    }
}