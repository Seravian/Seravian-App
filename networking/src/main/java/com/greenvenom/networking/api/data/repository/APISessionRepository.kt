package com.greenvenom.networking.api.data.repository

import com.greenvenom.networking.data.SessionDestinations
import com.greenvenom.networking.domain.repository.SessionStateRepository
import io.ktor.client.HttpClient
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class APISessionRepository(apiClient: HttpClient): SessionStateRepository {
    private val client = apiClient
    private val _userSessionDestination = Channel<SessionDestinations>(Channel.CONFLATED)
    override val userSessionDestination: Flow<SessionDestinations> = _userSessionDestination.receiveAsFlow()

    override fun collectSessionStatus() {
        TODO("Not yet implemented")
    }
}