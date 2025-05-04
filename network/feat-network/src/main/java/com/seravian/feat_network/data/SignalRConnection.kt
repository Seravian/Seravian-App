package com.seravian.feat_network.data

import android.util.Log
import com.greenvenom.core_network.api.utils.constructUrl
import com.greenvenom.core_network.api.utils.safeCall
import com.greenvenom.core_network.data.map
import com.greenvenom.core_network.domain.ConnectionStatus
import com.greenvenom.core_tokens.data.dto.response.TokensResponse
import com.greenvenom.core_tokens.domain.Tokens
import com.greenvenom.core_tokens.domain.repo.TokenDataSource
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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SignalRConnection(
    private val tokensDataSource: TokenDataSource,
    private val httpClient: HttpClient,
) {
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val defaultRetryDelays = listOf(2_000L, 3_000L, 5_000L, 10_000L)

    private val currentTokenFlow = MutableStateFlow(Tokens())
    private val tokenMutex = Mutex()

    private val _connectionStatus = MutableStateFlow(ConnectionStatus.IDLE)
    val connectionStatus: StateFlow<ConnectionStatus> = _connectionStatus.asStateFlow()

    private var tokenCollectionJob: Job? = null
    private var statusCollectionJob: Job? = null

    lateinit var hubConnection: HubConnection

    suspend fun connect(): HubConnection {
        // Start the token collection if not already started
        if (tokenCollectionJob == null) {
            tokenCollectionJob = scope.launch {
                tokensDataSource.getStoredTokensFlow().collectLatest { storedTokens ->
                    tokenMutex.withLock {
                        val refreshedTokens = refreshTokenIfNeeded(storedTokens)
                        currentTokenFlow.value = refreshedTokens
                    }
                }
            }
        }

        val validToken = currentTokenFlow
            .filter { it.accessToken.isNotBlank() && !it.refreshToken.isNullOrEmpty() }
            .onEach { Log.d("Creation", "Valid token: ${it.accessToken}, ${it.refreshToken}") }
            .first() // suspend until this condition is true

        // Create the hub connection
        hubConnection = HubConnectionBuilder.create(constructUrl("hubs/chat")) {
            Log.d("Creation", "Creating hub connection ${currentTokenFlow.value.accessToken}")
            accessToken = validToken.accessToken

            automaticReconnect = AutomaticReconnect.Custom { previousRetryCount, _ ->
                Log.d("Creation", "Reconnect")

                // Attempt to refresh token on reconnect if needed
                scope.launch {
                    tokenMutex.withLock {
                        val tokens = currentTokenFlow.value
                        val refreshedTokens = refreshTokenIfNeeded(tokens)
                        accessToken = refreshedTokens.accessToken
                    }
                }

                defaultRetryDelays.getOrNull(previousRetryCount)
            }
        }

        // Start the connection
        hubConnection.start()
        hubConnection.connectionId

        return hubConnection
    }

    fun startCollectingConnectionStatus() {
        if (statusCollectionJob == null) {
            statusCollectionJob = scope.launch {
                hubConnection.connectionState.collect { connectionState ->
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

    suspend fun disconnect() {
        statusCollectionJob?.cancel()
        statusCollectionJob = null

        tokenCollectionJob?.cancel()
        tokenCollectionJob = null

        if (::hubConnection.isInitialized) {
            hubConnection.stop()
        }

        _connectionStatus.update { ConnectionStatus.IDLE }
    }

    private suspend fun refreshTokenIfNeeded(currentTokens: Tokens): Tokens {
        if (currentTokens.isAccessExpired() && !currentTokens.refreshToken.isNullOrEmpty()) {
            val refreshRequest = currentTokens.toRefreshTokenRequest()
            val newTokensResult = safeCall<TokensResponse> {
                httpClient.post(constructUrl("auth/refresh-token")) {
                    setBody(refreshRequest)
                }
            }.map { it.extractTokens() }

            newTokensResult.map { tokens ->
                tokensDataSource.saveTokensLocally(tokens)
                Log.d("Creation", "${tokens.accessToken}, ${tokens.refreshToken}")
                return tokens
            }
        }
        return currentTokens
    }
}