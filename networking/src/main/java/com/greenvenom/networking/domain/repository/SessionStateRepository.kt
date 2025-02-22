package com.greenvenom.networking.domain.repository

import com.greenvenom.networking.data.SessionDestinations
import kotlinx.coroutines.flow.StateFlow

interface SessionStateRepository {
    val userSessionDestination: StateFlow<SessionDestinations>

    fun collectSessionStatus()
}