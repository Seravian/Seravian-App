package com.seravian.feat_profile.data

import com.greenvenom.core_network.api.utils.constructUrl
import com.greenvenom.core_network.api.utils.safeCall
import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.onSuccess
import com.seravian.core_profile.data.remote.request.LogoutRequest
import com.seravian.feat_profile.domain.ProfileRemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class SeravianProfileDataSource(
    private val authorizedHttpClient: HttpClient
): ProfileRemoteDataSource {
    override suspend fun logoutUser(logoutRequest: LogoutRequest): EmptyResult<NetworkError> {
        return safeCall<Unit> {
            authorizedHttpClient.post(constructUrl("auth/logout")) {
                setBody(logoutRequest)
            }
        }.onSuccess { authorizedHttpClient.authProvider<BearerAuthProvider>()?.clearToken() }
    }
}