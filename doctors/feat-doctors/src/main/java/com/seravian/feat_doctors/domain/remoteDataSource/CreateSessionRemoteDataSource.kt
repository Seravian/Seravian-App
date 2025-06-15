package com.seravian.feat_doctors.domain.remoteDataSource

import com.seravian.core_network.data.NetworkError
import com.seravian.core_network.data.NetworkResult
import com.seravian.core_doctors.data.dto.request.CreateSessionRequest
import com.seravian.core_doctors.data.dto.response.CreateSessionResponse

interface CreateSessionRemoteDataSource {
    suspend fun createSession(
        createSessionRequest: CreateSessionRequest
    ): NetworkResult<CreateSessionResponse, NetworkError>
}