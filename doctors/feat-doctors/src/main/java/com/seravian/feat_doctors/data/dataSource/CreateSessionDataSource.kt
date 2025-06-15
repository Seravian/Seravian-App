package com.seravian.feat_doctors.data.dataSource

import com.greenvenom.core_network.api.utils.constructUrl
import com.greenvenom.core_network.api.utils.safeCall
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_doctors.data.dto.request.CreateSessionRequest
import com.seravian.core_doctors.data.dto.response.CreateSessionResponse
import com.seravian.feat_doctors.domain.remoteDataSource.CreateSessionRemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class CreateSessionDataSource(
    private val authorizedHttpClient : HttpClient
) : CreateSessionRemoteDataSource{
    override suspend fun createSession(createSessionRequest: CreateSessionRequest): NetworkResult<CreateSessionResponse, NetworkError> {
        return safeCall {
            authorizedHttpClient.post(constructUrl("patientsessions/create-session-booking")){
                setBody(createSessionRequest)
            }
        }
    }
}