package com.seravian.feat_network.util

import com.greenvenom.core_network.api.utils.applyBaseConfig
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_network.domain.TokenRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import org.koin.mp.KoinPlatform.getKoin

object HttpClientFactory {
    fun publicClient(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            applyBaseConfig()
        }
    }
    fun authorizedClient(engine: HttpClientEngine, tokenRepoProvider: () -> TokenRepository): HttpClient {
        return HttpClient(engine) {
            applyBaseConfig()

            install(Auth) {
                bearer {
                    loadTokens {
                        val tokenInfo = tokenRepoProvider().getStoredToken()
                        tokenInfo?.let {
                            BearerTokens(it.accessToken, it.refreshToken)
                        }
                    }

                    refreshTokens {
                        val tokenInfo = tokenRepoProvider().getStoredToken()
                        var bearerTokens = BearerTokens("", "")
                        tokenInfo?.let { info ->
                            val newToken = tokenRepoProvider().refreshToken(info.refreshToken)
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

//    fun authorizedClient(engine: HttpClientEngine): HttpClient {
//        val tokenRepository = getKoin().get<TokenRepository>()
//        return HttpClient(engine) {
//            applyBaseConfig()
//
//            install(Auth) {
//                bearer {
//                    loadTokens {
//                        val tokenInfo = tokenRepository.getStoredToken()
//                        tokenInfo?.let { info ->
//                            BearerTokens(info.accessToken, info.refreshToken)
//                        }
//                    }
//
//                    refreshTokens {
//                        var bearerTokens = BearerTokens("", "")
//                        val tokenInfo = tokenRepository.getStoredToken()
//                        tokenInfo?.let { info ->
//                            val newToken = tokenRepository.refreshToken(info.refreshToken)
//                            newToken.onSuccess { token ->
//                                bearerTokens = BearerTokens(token.accessToken, token.refreshToken)
//                            }
//                            bearerTokens
//                        } ?: throw Exception()
//                    }
//                }
//            }
//        }
//    }
}