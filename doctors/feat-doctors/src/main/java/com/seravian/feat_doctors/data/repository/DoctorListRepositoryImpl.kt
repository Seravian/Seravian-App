package com.seravian.feat_doctors.data.repository

import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.map
import com.greenvenom.core_network.data.onSuccess
import com.seravian.core_doctors.data.dto.request.GetDoctorRequest
import com.seravian.core_doctors.domain.models.Doctor
import com.seravian.feat_doctors.domain.DoctorRemoteDataSource
import com.seravian.feat_doctors.domain.repository.DoctorRepository

class DoctorListRepositoryImpl(
    private val seravianDoctorDataSource: DoctorRemoteDataSource,
) : DoctorRepository {

    override suspend fun getDoctors(): NetworkResult<List<Doctor>, NetworkError> {
        val doctorsList = seravianDoctorDataSource.getDoctors()
        return doctorsList
            .map { doctors -> doctors.map { it.extractDoctor() } }

    }

    override suspend fun getDoctorsDetails(getDoctorRequest: GetDoctorRequest): NetworkResult<Doctor, NetworkError> {
        val currentDoctor = seravianDoctorDataSource.getDoctor(getDoctorRequest)
        return currentDoctor
            .map { doctor -> doctor.extractDoctor() }
    }
}