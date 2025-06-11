package com.seravian.feat_verification.data.repo

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_verification.data.dto.request.DeleteVerificationRequest
import com.seravian.core_verification.data.dto.request.GetVerificationsRequest
import com.seravian.core_verification.data.dto.request.VerificationRequest
import com.seravian.core_verification.data.dto.response.VerificationResponse
import com.seravian.feat_verification.domain.repo.VerificationRepository
import com.seravian.feat_verification.domain.source.VerificationDataSource

class VerificationRepositoryImpl(
    private val verificationDataSource: VerificationDataSource
): VerificationRepository {
    override suspend fun getVerificationRequests(): NetworkResult<List<VerificationResponse>, NetworkError> {
        return verificationDataSource.getVerificationRequests()
    }

    override suspend fun getVerificationRequest(request: GetVerificationsRequest): NetworkResult<VerificationResponse, NetworkError> {
        return verificationDataSource.getVerificationRequest(request)
    }

    override suspend fun sendVerificationRequest(request: VerificationRequest): EmptyResult<NetworkError> {
        return verificationDataSource.sendVerificationRequest(request)
    }

    override suspend fun deleteVerificationRequest(request: DeleteVerificationRequest): EmptyResult<NetworkError> {
        return verificationDataSource.deleteVerificationRequest(request)
    }
}