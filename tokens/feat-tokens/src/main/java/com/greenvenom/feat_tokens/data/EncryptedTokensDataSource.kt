package com.greenvenom.feat_tokens.data

import android.content.Context
import android.util.Log
import androidx.datastore.dataStore
import com.greenvenom.core_network.api.utils.constructUrl
import com.greenvenom.core_network.api.utils.safeCall
import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.map
import com.greenvenom.core_tokens.data.dto.request.RefreshTokenRequest
import com.greenvenom.core_tokens.data.dto.response.TokensResponse
import com.greenvenom.core_tokens.domain.repo.TokensDataSource
import com.greenvenom.core_tokens.domain.Tokens
import com.greenvenom.feat_tokens.utils.TokensSerializer
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.io.IOException
import java.io.File

class EncryptedTokensDataSource(
    private val publicHttpClient: HttpClient,
    private val context: Context
): TokensDataSource {
    private val TAG = "EncryptedTokensDataSource"
    private val Context.tokenDataStore by dataStore("tokens.pb", TokensSerializer)

    private val mutex = Mutex()
    private var currentExecution: Deferred<EmptyResult<NetworkError>>? = null

    override suspend fun refreshTokens(
        refreshTokenRequest: RefreshTokenRequest
    ): EmptyResult<NetworkError> {
        // Fast path: if a refresh is already in progress, wait for it
        currentExecution?.let { existing ->
            println("RefreshTokens: Joining existing token refresh...")
            return existing.await()
        }

        return mutex.withLock {
            currentExecution?.let { existing ->
                return existing.await()
            }

            try {
                val deferred = CoroutineScope(Dispatchers.IO).async {
                    safeCall<TokensResponse> {
                        publicHttpClient.post(constructUrl("auth/refresh-token")) {
                            setBody(refreshTokenRequest)
                        }
                    }.map { tokensResponse ->
                        saveTokensLocally(tokensResponse.extractTokens())
                    }
                }

                currentExecution = deferred
                deferred.await()
            } finally {
                currentExecution = null
            }
        }
    }

    override suspend fun getStoredTokens(): Tokens {
        return try {
            context.tokenDataStore.data
                .catch { exception ->
                    if (exception is IOException ||
                        exception.cause is javax.crypto.BadPaddingException ||
                        exception.cause is android.security.KeyStoreException ||
                        exception.toString().contains("BadPaddingException") ||
                        exception.toString().contains("KeyStoreException")) {

                        Log.e(TAG, "Error decrypting tokens, resetting to default: ${exception.message}")
                        // If there's a decryption error, emit the default empty tokens
                        emit(Tokens())

                        // Then clear the corrupt data
                        deleteTokens()
                    } else {
                        // For other exceptions, rethrow
                        throw exception
                    }
                }
                .first()
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error getting tokens: ${e.message}")
            Tokens()
        }
    }

    override fun getStoredTokensFlow(): Flow<Tokens> {
        return context.tokenDataStore.data
            .catch { exception ->
                if (exception is IOException ||
                    exception.cause is javax.crypto.BadPaddingException ||
                    exception.cause is android.security.KeyStoreException ||
                    exception.toString().contains("BadPaddingException") ||
                    exception.toString().contains("KeyStoreException")) {

                    Log.e(TAG, "Error reading token flow, resetting to default: ${exception.message}")
                    // If there's a decryption error, emit the default empty tokens
                    emit(Tokens())

                    // Then clear the corrupt data in a coroutine
                    CoroutineScope(Dispatchers.IO).launch {
                        deleteTokens()
                    }
                } else {
                    // For other exceptions, rethrow
                    throw exception
                }
            }
    }

    override suspend fun saveTokensLocally(tokens: Tokens) {
        try {
            context.tokenDataStore.updateData {
                Tokens(
                    accessToken = tokens.accessToken,
                    accessTokenExpirationUtc = tokens.accessTokenExpirationUtc,
                    refreshToken = tokens.refreshToken
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error saving tokens, attempting to clear and retry: ${e.message}")

            // If saving fails, try to delete and then save again
            try {
                // First delete tokens
                deleteTokens()

                // Then try to save again
                context.tokenDataStore.updateData {
                    Tokens(
                        accessToken = tokens.accessToken,
                        accessTokenExpirationUtc = tokens.accessTokenExpirationUtc,
                        refreshToken = tokens.refreshToken
                    )
                }
            } catch (e2: Exception) {
                Log.e(TAG, "Failed to save tokens even after reset: ${e2.message}")
                // At this point, we've tried our best
            }
        }
    }

    override suspend fun deleteTokens() {
        try {
            // Regular way to clear tokens by setting to default value
            context.tokenDataStore.updateData { Tokens() }

            // For more aggressive clearing if needed, try to delete the actual file
            // This is optional, but can help if the DataStore file itself is corrupted
            try {
                val dataStoreFile = File("${context.filesDir}/datastore/tokens.pb")
                if (dataStoreFile.exists()) {
                    val deleted = dataStoreFile.delete()
                    Log.d(TAG, "DataStore file deleted: $deleted")
                }
            } catch (fileEx: Exception) {
                Log.e(TAG, "Error deleting DataStore file: ${fileEx.message}")
                // We already tried updating data, so just log this error
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting tokens: ${e.message}")
            // If updateData fails, try the file deletion method as fallback
            try {
                val dataStoreFile = File("${context.filesDir}/datastore/tokens.pb")
                if (dataStoreFile.exists()) {
                    val deleted = dataStoreFile.delete()
                    Log.d(TAG, "DataStore file deleted as fallback: $deleted")
                }
            } catch (fileEx: Exception) {
                Log.e(TAG, "Error with fallback deletion: ${fileEx.message}")
            }
        }
    }
}