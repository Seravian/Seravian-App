package com.seravian.feat_profile.domain

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.seravian.core_profile.data.remote.request.LogoutRequest

interface ProfileRemoteDataSource {
    suspend fun logoutUser(logoutRequest: LogoutRequest): EmptyResult<NetworkError>
}