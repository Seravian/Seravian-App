package com.seravian.feat_doctors.presentation.viewModel.detailsDoctor


import androidx.compose.runtime.Immutable
import com.seravian.feat_doctors.presentation.model.Doctor

@Immutable
data class DoctorDetailsState(
    val doctor: Doctor? = null,
)