package com.seravian.feat_doctors.domain

import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_doctors.data.dto.request.GetDoctorRequest
import com.seravian.core_doctors.data.dto.response.GetDoctorResponse

interface DoctorRemoteDataSource {

    suspend fun getDoctors():NetworkResult<List<GetDoctorResponse>,NetworkError>

    suspend fun getDoctor(
        getDoctorRequest: GetDoctorRequest
    ):NetworkResult<GetDoctorResponse,NetworkError>
}