package com.greenvenom.core_network.domain

import com.greenvenom.core_network.data.ConnectionStatus
import eu.lepicekmichal.signalrkore.HubConnection
import kotlinx.coroutines.flow.StateFlow

interface RealtimeConnection {
    var connection: HubConnection
    val connectionStatus: StateFlow<ConnectionStatus>

    suspend fun connect()
    fun startCollectingConnectionStatus()
    suspend fun disconnect()
}