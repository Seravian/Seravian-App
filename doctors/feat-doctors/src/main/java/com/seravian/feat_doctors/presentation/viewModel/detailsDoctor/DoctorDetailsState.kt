package com.seravian.feat_doctors.presentation.viewModel.detailsDoctor


import androidx.compose.runtime.Immutable
import com.seravian.core_doctors.domain.models.Doctor

@Immutable
data class DoctorDetailsState(
    val doctor: Doctor? = null,
)