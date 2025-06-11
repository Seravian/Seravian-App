package com.seravian.feat_doctors.presentation.viewModel.detailsDoctor

sealed interface DoctorDetailsAction {
    data object BackToDoctorsList : DoctorDetailsAction
    data class BookSession(
        val patientIsAvailableFromUtc: String,
        val patientIsAvailableToUtc: String,
        val patientNote: String
    ) : DoctorDetailsAction
}