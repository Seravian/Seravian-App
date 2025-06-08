package com.seravian.feat_doctors.presentation.viewModel.detailsDoctor

sealed interface DoctorDetailsAction {
    data object BackToDoctorsList : DoctorDetailsAction
}