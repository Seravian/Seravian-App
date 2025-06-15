package com.seravian.feat_profile.data

import com.seravian.core_network.api.utils.constructUrl
import com.seravian.core_network.api.utils.safeCall
import com.seravian.core_network.data.EmptyResult
import com.seravian.core_network.data.NetworkError
import com.seravian.core_network.data.NetworkResult
import com.seravian.core_network.data.onSuccess
import com.seravian.core_profile.data.remote.request.LogoutRequest
import com.seravian.core_profile.data.remote.response.DoctorProfileResponse
import com.seravian.feat_profile.domain.ProfileRemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class SeravianProfileDataSource(
    private val authorizedHttpClient: HttpClient
): ProfileRemoteDataSource {
    override suspend fun getDoctorProfile(): NetworkResult<DoctorProfileResponse, NetworkError> {
        return safeCall {
            authorizedHttpClient.get(constructUrl("doctor/profile"))
        }
    }

    override suspend fun logoutUser(logoutRequest: LogoutRequest): EmptyResult<NetworkError> {
        return safeCall<Unit> {
            authorizedHttpClient.post(constructUrl("auth/logout")) {
                setBody(logoutRequest)
            }
        }.onSuccess { authorizedHttpClient.authProvider<BearerAuthProvider>()?.clearToken() }
    }
}