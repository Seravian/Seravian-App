package com.seravian.feat_network.util

import com.greenvenom.core_network.api.utils.applyBaseConfig
import com.greenvenom.core_network.api.utils.constructUrl
import com.greenvenom.core_network.api.utils.safeCall
import com.greenvenom.core_network.data.ErrorType
import com.greenvenom.core_network.data.map
import com.greenvenom.core_network.data.onError
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_tokens.data.dto.response.TokensResponse
import com.greenvenom.core_tokens.domain.repo.TokenDataSource
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.request.post
import io.ktor.client.request.setBody

object HttpClientFactory {
    fun publicClient(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            applyBaseConfig()
        }
    }

    fun authorizedClient(
        engine: HttpClientEngine,
        tokensDataSource: TokenDataSource,
    ): HttpClient {
        return HttpClient(engine) {
            applyBaseConfig()

            install(Auth) {
                bearer {
                    loadTokens {
                        val tokenInfo = tokensDataSource.getStoredTokens()
                        tokenInfo?.let { info ->
                            BearerTokens(info.accessToken, info.refreshToken)
                        }
                    }

                    refreshTokens {
                        val tokens = tokensDataSource.getStoredTokens() ?: return@refreshTokens null

                        val newTokensResult = safeCall<TokensResponse> {
                            this.client.post(urlString = constructUrl("auth/refresh-token")) {
                                setBody(tokens.toRefreshTokenRequest())
                            }
                        }.map { it.extractTokens() }

                        var bearerTokens: BearerTokens? = null
                        newTokensResult
                            .onSuccess {
                                tokensDataSource.saveTokensLocally(it)
                                bearerTokens = BearerTokens(it.accessToken, it.refreshToken)
                            }
                            .onError { error ->
                                if (error.errorType == ErrorType.BAD_REQUEST) {
                                    bearerTokens = null
                                    tokensDataSource.deleteTokens()
                                }
                            }

                        bearerTokens
                    }
                }
            }
        }
    }
}