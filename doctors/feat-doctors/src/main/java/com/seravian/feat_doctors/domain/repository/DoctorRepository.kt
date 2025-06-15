package com.seravian.feat_doctors.domain.repository

import com.seravian.core_network.data.NetworkError
import com.seravian.core_network.data.NetworkResult
import com.seravian.core_doctors.data.dto.request.GetDoctorRequest
import com.seravian.core_doctors.domain.models.Doctor

interface DoctorRepository {
    suspend fun getDoctors(): NetworkResult<List<Doctor>, NetworkError>

    suspend fun getDoctorsDetails(getDoctorRequest: GetDoctorRequest): NetworkResult<Doctor, NetworkError>
}