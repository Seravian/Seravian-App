package com.seravian.feat_network.data.local

import android.content.Context
import androidx.datastore.dataStore
import com.greenvenom.core_network.data.TokenInfo
import com.seravian.feat_network.domain.local.TokenDataSource
import com.seravian.feat_network.util.TokenSerializer
import kotlinx.coroutines.flow.firstOrNull

class EncryptedTokenDataSource(
    private val context: Context
): TokenDataSource {
    private val Context.tokenDataStore by dataStore("token", TokenSerializer)

    override suspend fun getStoredToken(): TokenInfo? {
        return context.tokenDataStore.data.firstOrNull()
    }

    override suspend fun saveTokenLocally(tokenInfo: TokenInfo) {
        context.tokenDataStore.updateData { currentToken ->
            currentToken.copy(
                accessToken = tokenInfo.accessToken,
                accessExpiresIn = tokenInfo.accessExpiresIn,
                refreshToken = tokenInfo.refreshToken
            )
        }
    }

    override suspend fun deleteToken() {
        context.tokenDataStore.updateData { TokenInfo() }
    }
}