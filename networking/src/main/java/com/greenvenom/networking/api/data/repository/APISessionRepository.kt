package com.greenvenom.networking.api.data.repository

import com.greenvenom.networking.data.SessionDestinations
import com.greenvenom.networking.domain.repository.SessionStateRepository
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class APISessionRepository(apiClient: HttpClient): SessionStateRepository {
    private val client = apiClient
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _userSessionDestination = MutableStateFlow(SessionDestinations.INITIALIZE)
    override val userSessionDestination: StateFlow<SessionDestinations> = _userSessionDestination
        .stateIn(scope, SharingStarted.WhileSubscribed(5000), SessionDestinations.INITIALIZE)

    override fun collectSessionStatus() {
        TODO("Not yet implemented")
    }
}