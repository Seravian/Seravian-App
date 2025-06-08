package com.seravian.feat_doctors.presentation.viewModel.doctor

import androidx.compose.runtime.Immutable
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.feat_doctors.presentation.model.Doctor

@Immutable
data class DoctorState(
    val doctors: List<Doctor> = emptyList(),
    val getDoctorsListResult : NetworkResult<List<Doctor>,NetworkError>?=null
)