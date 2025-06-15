package com.seravian.feat_verification.data.repo

import com.seravian.core_network.data.EmptyResult
import com.seravian.core_network.data.NetworkError
import com.seravian.core_network.data.NetworkResult
import com.seravian.core_network.data.map
import com.seravian.core_verification.data.dto.request.DeleteVerificationRequest
import com.seravian.core_verification.data.dto.request.GetVerificationsRequest
import com.seravian.core_verification.data.dto.request.VerificationRequest
import com.seravian.core_verification.domain.Verification
import com.seravian.feat_verification.domain.repo.VerificationRepository
import com.seravian.feat_verification.domain.source.VerificationDataSource

class VerificationRepositoryImpl(
    private val verificationDataSource: VerificationDataSource
): VerificationRepository {
    override suspend fun getVerificationRequests(): NetworkResult<List<Verification>, NetworkError> {
        return verificationDataSource.getVerificationRequests().map {
            it.map { response -> response.extractVerification() }
        }
    }

    override suspend fun getVerificationRequest(
        request: GetVerificationsRequest
    ): NetworkResult<Verification, NetworkError> {
        return verificationDataSource.getVerificationRequest(request).map {
            it.extractVerification()
        }
    }

    override suspend fun sendVerificationRequest(
        request: VerificationRequest
    ): NetworkResult<Long, NetworkError> {
        return verificationDataSource.sendVerificationRequest(request).map {
            it.verificationRequestId
        }
    }

    override suspend fun deleteVerificationRequest(
        request: DeleteVerificationRequest
    ): EmptyResult<NetworkError> {
        return verificationDataSource.deleteVerificationRequest(request)
    }
}