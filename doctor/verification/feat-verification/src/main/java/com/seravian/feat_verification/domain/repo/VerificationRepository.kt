package com.seravian.feat_verification.domain.repo

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_verification.data.dto.request.DeleteVerificationRequest
import com.seravian.core_verification.data.dto.request.GetVerificationsRequest
import com.seravian.core_verification.data.dto.request.VerificationRequest
import com.seravian.core_verification.data.dto.response.VerificationResponse

interface VerificationRepository {
    suspend fun getVerificationRequests(): NetworkResult<List<VerificationResponse>, NetworkError>

    suspend fun getVerificationRequest(
        request: GetVerificationsRequest
    ): NetworkResult<VerificationResponse, NetworkError>

    suspend fun sendVerificationRequest(
        request: VerificationRequest
    ): EmptyResult<NetworkError>

    suspend fun deleteVerificationRequest(
        request: DeleteVerificationRequest
    ): EmptyResult<NetworkError>
}