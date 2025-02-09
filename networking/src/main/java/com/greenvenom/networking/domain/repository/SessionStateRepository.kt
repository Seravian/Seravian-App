package com.greenvenom.networking.domain.repository

import com.greenvenom.networking.data.SessionDestinations
import kotlinx.coroutines.flow.Flow

interface SessionStateRepository {
    val userSessionDestination: Flow<SessionDestinations>

    fun collectSessionStatus()
}