package com.seravian.feat_doctors.presentation.viewModel.doctor

import androidx.compose.runtime.Immutable
import com.seravian.core_network.data.NetworkError
import com.seravian.core_network.data.NetworkResult
import com.seravian.core_doctors.domain.models.Doctor

@Immutable
data class DoctorState(
    val doctors: List<Doctor> = emptyList(),
    val getDoctorsListResult : NetworkResult<List<Doctor>,NetworkError>?=null
)