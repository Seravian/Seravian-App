package com.seravian.feat_doctors.presentation.viewModel.doctor

import com.seravian.feat_doctors.presentation.model.Doctor

sealed interface DoctorAction {
    data class OnDoctorClick(val doctor: Doctor) : DoctorAction
}