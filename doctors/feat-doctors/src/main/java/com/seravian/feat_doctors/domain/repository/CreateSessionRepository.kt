package com.seravian.feat_doctors.domain.repository

import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_doctors.data.dto.request.CreateSessionRequest
import com.seravian.core_doctors.domain.models.Session

interface CreateSessionRepository {
    suspend fun createSession(createSessionRequest: CreateSessionRequest): NetworkResult<Session,NetworkError>
}