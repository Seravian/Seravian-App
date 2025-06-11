package com.seravian.feat_doctors.data.dataSource

import com.greenvenom.core_network.api.utils.constructUrl
import com.greenvenom.core_network.api.utils.safeCall
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_doctors.data.dto.request.GetDoctorRequest
import com.seravian.core_doctors.data.dto.response.GetDoctorResponse
import com.seravian.feat_doctors.domain.remoteDataSource.DoctorRemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class SeravianDoctorDataSource(
    private val authorizedHttpClient: HttpClient
) : DoctorRemoteDataSource {
    override suspend fun getDoctors(): NetworkResult<List<GetDoctorResponse>, NetworkError> {
        return safeCall {
            authorizedHttpClient.get(constructUrl("patientsessions/get-doctors"))
        }
    }

    override suspend fun getDoctor(getDoctorRequest: GetDoctorRequest): NetworkResult<GetDoctorResponse, NetworkError> {
        return safeCall {
            authorizedHttpClient.get(constructUrl("patientsessions/get-doctor")){
                url {
                    parameters.append("doctorId",getDoctorRequest.id)
                }
            }
        }
    }
}