package com.seravian.feat_network.util

import com.seravian.core_network.api.utils.applyBaseConfig
import com.seravian.core_network.data.ErrorType
import com.seravian.core_network.data.onError
import com.seravian.core_network.data.onSuccess
import com.seravian.core_tokens.domain.repo.TokensDataSource
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer

object ClientFactory {
    fun publicClient(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            applyBaseConfig()
        }
    }

    fun authorizedClient(
        engine: HttpClientEngine,
        tokensDataSource: TokensDataSource,
    ): HttpClient {
        return HttpClient(engine) {
            applyBaseConfig()

            install(Auth) {
                bearer {
                    loadTokens {
                        val tokenInfo = tokensDataSource.getStoredTokens()
                        tokenInfo.let { info ->
                            BearerTokens(info.accessToken, info.refreshToken)
                        }
                    }

                    refreshTokens {
                        var tokens = tokensDataSource.getStoredTokens()

                        val newTokensResult = tokensDataSource.refreshTokens(tokens.toRefreshTokenRequest())

                        var bearerTokens: BearerTokens? = null
                        newTokensResult
                            .onSuccess {
                                tokens = tokensDataSource.getStoredTokens()
                                bearerTokens = BearerTokens(tokens.accessToken, tokens.refreshToken)
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