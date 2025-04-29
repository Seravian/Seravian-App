package com.greenvenom.feat_tokens.data

import android.content.Context
import androidx.datastore.dataStore
import com.greenvenom.core_tokens.domain.repo.TokenDataSource
import com.greenvenom.core_tokens.domain.Tokens
import com.greenvenom.feat_tokens.utils.TokensSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class EncryptedTokenDataSource(
    private val context: Context
): TokenDataSource {
    private val Context.tokenDataStore by dataStore("tokens", TokensSerializer)

    override suspend fun getStoredTokens(): Tokens? {
        return context.tokenDataStore.data.firstOrNull()
    }

    override fun getStoredTokensFlow(): Flow<Tokens?> {
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
            null
        }
    }
}