package com.seravian.feat_network.util

import com.greenvenom.core_network.api.utils.applyBaseConfig
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_network.domain.TokenRepository
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

    fun authorizedClient(engine: HttpClientEngine, tokenRepo: TokenRepository): HttpClient {
        return HttpClient(engine) {
            applyBaseConfig()

            install(Auth) {
                bearer {
                    loadTokens {
                        val tokenInfo = tokenRepo.getStoredToken()
                        tokenInfo?.let {
                            BearerTokens(it.accessToken, it.refreshToken)
                        }
                    }

                    refreshTokens {
                        val tokenInfo = tokenRepo.getStoredToken()
                        var bearerTokens = BearerTokens("", "")
                        tokenInfo?.let { info ->
                            val newToken = tokenRepo.refreshToken(info.refreshToken)
                            newToken.onSuccess {
                                bearerTokens = BearerTokens(it.accessToken, it.refreshToken)
                            }
                        }
                        bearerTokens
                    }
                }
            }
        }
    }
}