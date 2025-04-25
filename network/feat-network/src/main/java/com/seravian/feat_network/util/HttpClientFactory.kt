package com.seravian.feat_network.util

import com.greenvenom.core_network.api.utils.applyBaseConfig
import com.greenvenom.core_network.data.onError
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_network.domain.repository.TokensRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer

object HttpClientFactory {
    fun publicClient(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            applyBaseConfig()
        }
    }

    fun authorizedClient(engine: HttpClientEngine, tokensRepo: TokensRepository): HttpClient {
        return HttpClient(engine) {
            applyBaseConfig()

            install(Auth) {
                bearer {
                    loadTokens {
                        val tokenInfo = tokensRepo.getStoredTokens()
                        tokenInfo?.let { info ->
                            BearerTokens(info.accessToken, info.refreshToken)
                        }
                    }

                    refreshTokens {
                        val tokens = tokensRepo.getStoredTokens() ?: return@refreshTokens null

                        val newTokensResult = tokensRepo.refreshToken(tokens.toRefreshTokenRequest())
                        var bearerTokens: BearerTokens? = null
                        newTokensResult
                            .onSuccess {
                                tokensRepo.saveTokensLocally(it)
                                bearerTokens = BearerTokens(it.accessToken, it.refreshToken)
                            }
                            .onError { bearerTokens = null }

                        bearerTokens
                    }
                }
            }
        }
    }
}