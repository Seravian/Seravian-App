package com.seravian.feat_doctors.presentation.viewModel.doctor

import com.seravian.core_doctors.domain.models.Doctor


sealed interface DoctorAction {
    data class OnDoctorClick(val doctor: Doctor) : DoctorAction
}