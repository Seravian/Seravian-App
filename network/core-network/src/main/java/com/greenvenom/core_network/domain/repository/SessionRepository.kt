package com.greenvenom.core_network.domain.repository

import com.greenvenom.core_network.domain.SessionDestinations
import kotlinx.coroutines.flow.StateFlow

interface SessionRepository {
    val sessionDestination: StateFlow<SessionDestinations>

    fun collectSessionStatus()
}