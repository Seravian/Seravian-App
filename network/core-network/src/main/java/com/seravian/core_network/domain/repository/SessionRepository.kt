package com.seravian.core_network.domain.repository

import com.seravian.core_network.data.SessionDestinations
import kotlinx.coroutines.flow.StateFlow

interface SessionRepository {
    val sessionDestination: StateFlow<SessionDestinations>

    fun collectSessionStatus()
}