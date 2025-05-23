package com.seravian.feat_network.util

import android.util.Log
import com.greenvenom.core_network.api.utils.constructUrl
import com.greenvenom.core_network.api.utils.safeCall
import com.greenvenom.core_network.data.map
import com.greenvenom.core_network.data.ConnectionStatus
import com.greenvenom.core_network.data.ErrorType
import com.greenvenom.core_network.data.onError
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_network.domain.RealtimeConnection
import com.greenvenom.core_tokens.data.dto.response.TokensResponse
import com.greenvenom.core_tokens.domain.Tokens
import com.greenvenom.core_tokens.domain.repo.TokensDataSource
import eu.lepicekmichal.signalrkore.AutomaticReconnect
import eu.lepicekmichal.signalrkore.HubConnection
import eu.lepicekmichal.signalrkore.HubConnectionBuilder
import eu.lepicekmichal.signalrkore.HubConnectionState
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SignalRConnection(
    private val tokensDataSource: TokensDataSource,
): RealtimeConnection {
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val defaultRetryDelays = listOf(2_000L, 3_000L, 5_000L, 10_000L)

    private val currentTokenFlow = MutableStateFlow(Tokens())

    private val _connectionStatus = MutableStateFlow(ConnectionStatus.IDLE)
    override val connectionStatus: StateFlow<ConnectionStatus> = _connectionStatus.asStateFlow()

    private var tokenCollectionJob: Job? = null
    private var statusCollectionJob: Job? = null

    override lateinit var connection: HubConnection

    override suspend fun connect() {
        // Start the token collection if not already started
        if (tokenCollectionJob == null) {
            tokenCollectionJob = scope.launch {
                tokensDataSource.getStoredTokensFlow().collect { storedTokens ->
                    Log.d("EncryptedTokensDataSource", "Tokens: $storedTokens")
                    val refreshedTokens = refreshTokenIfNeeded(storedTokens)
                    currentTokenFlow.update { refreshedTokens }
                }
            }
        }

        val validToken = currentTokenFlow
            .filter { it.accessToken.isNotBlank() && !it.refreshToken.isNullOrEmpty() }
            .first() // suspend until this condition is true

        // Create the hub connection
        connection = HubConnectionBuilder.create(constructUrl("hubs/chat")) {
            accessToken = validToken.accessToken

            automaticReconnect = AutomaticReconnect.Custom { previousRetryCount, _ ->

                // Attempt to refresh token on reconnect if needed
                scope.launch {
                    val tokens = currentTokenFlow.value
                    val refreshedTokens = refreshTokenIfNeeded(tokens)
                    accessToken = refreshedTokens.accessToken
                }

                defaultRetryDelays.getOrNull(previousRetryCount)
            }
        }
        connection.start()
    }

    override fun startCollectingConnectionStatus() {
        if (statusCollectionJob == null) {
            statusCollectionJob = scope.launch {
                connection.connectionState.collect { connectionState ->
                    _connectionStatus.update {
                        when (connectionState) {
                            HubConnectionState.CONNECTED -> ConnectionStatus.CONNECTED
                            HubConnectionState.DISCONNECTED -> ConnectionStatus.DISCONNECTED
                            HubConnectionState.CONNECTING -> ConnectionStatus.CONNECTING
                            HubConnectionState.RECONNECTING -> ConnectionStatus.RECONNECTING
                            else -> ConnectionStatus.IDLE
                        }
                    }
                }
            }
        }
    }

    override suspend fun disconnect() {
        if (::connection.isInitialized) {
            connection.stop()
        }

        statusCollectionJob?.cancel()
        statusCollectionJob = null

        tokenCollectionJob?.cancel()
        tokenCollectionJob = null

        _connectionStatus.update { ConnectionStatus.IDLE }
    }

    private suspend fun refreshTokenIfNeeded(currentTokens: Tokens): Tokens {
        if (currentTokens.isAccessExpired() && !currentTokens.refreshToken.isNullOrEmpty()) {
            val newTokensResult = tokensDataSource.refreshTokens(currentTokens.toRefreshTokenRequest())

            newTokensResult
                .onSuccess {
                    return tokensDataSource.getStoredTokens()
                }
                .onError {
                    if (it.errorType == ErrorType.NO_INTERNET) refreshTokenIfNeeded(currentTokens)
                    else if (it.errorType == ErrorType.BAD_REQUEST) tokensDataSource.deleteTokens()
                }
        }
        return currentTokens
    }
}