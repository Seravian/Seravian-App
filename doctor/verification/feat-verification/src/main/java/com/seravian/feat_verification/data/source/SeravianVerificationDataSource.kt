package com.seravian.feat_verification.data.source

import com.seravian.core_network.api.utils.constructUrl
import com.seravian.core_network.api.utils.safeCall
import com.seravian.core_network.data.EmptyResult
import com.seravian.core_network.data.ErrorType
import com.seravian.core_network.data.NetworkError
import com.seravian.core_network.data.NetworkResult
import com.seravian.core_verification.data.dto.request.DeleteVerificationRequest
import com.seravian.core_verification.data.dto.request.GetVerificationsRequest
import com.seravian.core_verification.data.dto.request.VerificationRequest
import com.seravian.core_verification.data.dto.response.VerificationDetailsResponse
import com.seravian.core_verification.data.dto.response.VerificationRequestResponse
import com.seravian.feat_verification.domain.source.VerificationDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get

class SeravianVerificationDataSource(
    private val authorizedHttpClient: HttpClient
): VerificationDataSource {
    override suspend fun getVerificationRequests(): NetworkResult<List<VerificationDetailsResponse>, NetworkError> {
        return safeCall {
            authorizedHttpClient.get(constructUrl("doctor/get-doctor-verification-requests"))
        }
    }

    override suspend fun getVerificationRequest(
        request: GetVerificationsRequest
    ): NetworkResult<VerificationDetailsResponse, NetworkError> {
        return safeCall {
            authorizedHttpClient.get(constructUrl("doctor/get-doctor-verification-request")) {
                url {
                    parameters.append("requestId", request.requestId.toString())
                }
            }
        }
    }

    override suspend fun sendVerificationRequest(
        request: VerificationRequest
    ): NetworkResult<VerificationRequestResponse, NetworkError> {
        val formData = try {
            request.toMultiPartFormData()
        } catch (_: IllegalArgumentException) {
            return NetworkResult.Error(NetworkError(ErrorType.SERIALIZATION_ERROR))
        }

        return safeCall {
            authorizedHttpClient.submitFormWithBinaryData(
                url = constructUrl("doctor/send-doctor-verification-request"),
                formData = formData
            )
        }
    }

    override suspend fun deleteVerificationRequest(
        request: DeleteVerificationRequest
    ): EmptyResult<NetworkError> {
        return safeCall {
            authorizedHttpClient.delete(constructUrl("doctor/delete-doctor-verification-request")) {
                url {
                    parameters.append("requestId", request.requestId.toString())
                }
            }
        }
    }
}