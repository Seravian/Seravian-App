package com.seravian.feat_verification.domain.source

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_verification.data.dto.request.DeleteVerificationRequest
import com.seravian.core_verification.data.dto.request.GetVerificationsRequest
import com.seravian.core_verification.data.dto.request.VerificationRequest
import com.seravian.core_verification.data.dto.response.VerificationDetailsResponse
import com.seravian.core_verification.data.dto.response.VerificationRequestResponse

interface VerificationDataSource {
    suspend fun getVerificationRequests(): NetworkResult<List<VerificationDetailsResponse>, NetworkError>

    suspend fun getVerificationRequest(
        request: GetVerificationsRequest
    ): NetworkResult<VerificationDetailsResponse, NetworkError>

    suspend fun sendVerificationRequest(
        request: VerificationRequest
    ): NetworkResult<VerificationRequestResponse, NetworkError>

    suspend fun deleteVerificationRequest(
        request: DeleteVerificationRequest
    ): EmptyResult<NetworkError>
}