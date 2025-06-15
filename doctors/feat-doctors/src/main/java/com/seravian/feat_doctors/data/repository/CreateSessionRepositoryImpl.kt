package com.seravian.feat_doctors.data.repository

import com.seravian.core_network.data.NetworkError
import com.seravian.core_network.data.NetworkResult
import com.seravian.core_network.data.map
import com.seravian.core_doctors.data.dto.request.CreateSessionRequest
import com.seravian.core_doctors.domain.models.Session
import com.seravian.feat_doctors.domain.remoteDataSource.CreateSessionRemoteDataSource
import com.seravian.feat_doctors.domain.repository.CreateSessionRepository

class CreateSessionRepositoryImpl(
    private val createSessionDataSource: CreateSessionRemoteDataSource
) : CreateSessionRepository {
    override suspend fun createSession(createSessionRequest: CreateSessionRequest): NetworkResult<Session, NetworkError> {
        val createSessionResponse = createSessionDataSource.createSession(createSessionRequest)
        return createSessionResponse
            .map { response ->
                response.extractSession()
            }
    }
}