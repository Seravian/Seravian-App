package com.seravian.feat_profile.domain

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_profile.data.remote.request.LogoutRequest
import com.seravian.core_profile.data.remote.response.DoctorProfileResponse

interface ProfileRemoteDataSource {
    suspend fun getDoctorProfile(): NetworkResult<DoctorProfileResponse, NetworkError>

    suspend fun logoutUser(logoutRequest: LogoutRequest): EmptyResult<NetworkError>
}