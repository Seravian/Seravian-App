package com.seravian.feat_local.data

import android.content.Context
import androidx.datastore.dataStore
import com.seravian.core_local.data.TokensInfo
import com.seravian.core_local.domain.TokenDataSource
import com.seravian.feat_local.utils.TokensSerializer
import kotlinx.coroutines.flow.firstOrNull

class EncryptedTokenDataSource(
    private val context: Context
): TokenDataSource {
    private val Context.tokenDataStore by dataStore("tokens", TokensSerializer)

    override suspend fun getStoredToken(): TokensInfo? {
        return context.tokenDataStore.data.firstOrNull()
    }

    override suspend fun saveTokenLocally(tokensInfo: TokensInfo) {
        context.tokenDataStore.updateData { currentToken ->
            currentToken.copy(
                accessToken = tokensInfo.accessToken,
                accessExpiresIn = tokensInfo.accessExpiresIn,
                refreshToken = tokensInfo.refreshToken
            )
        }
    }

    override suspend fun deleteToken() {
        context.tokenDataStore.updateData {
            TokensInfo(
                accessToken = "",
                accessExpiresIn = "",
                refreshToken = ""
            )
        }
    }
}