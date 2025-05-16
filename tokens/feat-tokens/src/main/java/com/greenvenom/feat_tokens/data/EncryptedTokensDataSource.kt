package com.greenvenom.feat_tokens.data

import android.content.Context
import androidx.datastore.dataStore
import com.greenvenom.core_network.api.utils.constructUrl
import com.greenvenom.core_network.api.utils.safeCall
import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.map
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_tokens.data.dto.request.RefreshTokenRequest
import com.greenvenom.core_tokens.data.dto.response.TokensResponse
import com.greenvenom.core_tokens.domain.repo.TokensDataSource
import com.greenvenom.core_tokens.domain.Tokens
import com.greenvenom.feat_tokens.utils.TokensSerializer
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class EncryptedTokensDataSource(
    private val publicHttpClient: HttpClient,
    private val context: Context
): TokensDataSource {
    private val Context.tokenDataStore by dataStore("tokens", TokensSerializer)

    override suspend fun refreshTokens(refreshTokenRequest: RefreshTokenRequest): EmptyResult<NetworkError> {
        return safeCall<TokensResponse> {
            publicHttpClient.post(constructUrl("auth/refresh-token")) {
                setBody(refreshTokenRequest)
            }
        }.map { tokensResponse ->  saveTokensLocally(tokensResponse.extractTokens()) }
    }

    override suspend fun getStoredTokens(): Tokens {
        return context.tokenDataStore.data.first()
    }

    override fun getStoredTokensFlow(): Flow<Tokens> {
        return context.tokenDataStore.data
    }

    override suspend fun saveTokensLocally(tokens: Tokens) {
        context.tokenDataStore.updateData {
            Tokens(
                accessToken = tokens.accessToken,
                accessTokenExpirationUtc = tokens.accessTokenExpirationUtc,
                refreshToken = tokens.refreshToken
            )
        }
    }

    override suspend fun deleteTokens() {
        context.tokenDataStore.updateData {
            Tokens()
        }
    }
}