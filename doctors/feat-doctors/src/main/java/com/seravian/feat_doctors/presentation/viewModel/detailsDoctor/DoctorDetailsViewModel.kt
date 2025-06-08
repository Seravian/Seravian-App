package com.seravian.feat_doctors.presentation.viewModel.detailsDoctor

import com.greenvenom.core_ui.presentation.BaseViewModel
import com.seravian.feat_doctors.data.repository.DoctorsStateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DoctorDetailsViewModel(
    private val doctorsStateRepository: DoctorsStateRepository
):BaseViewModel() {
    private val _doctorDetailsState = MutableStateFlow(DoctorDetailsState())
    val doctorDetailsState = _doctorDetailsState.asStateFlow()


    init {
        _doctorDetailsState.update {
            it.copy(
                doctor = doctorsStateRepository.doctorsState.value.currentDoctor
            )
        }
    }

    fun doctorDetailsAction(action: DoctorDetailsAction) {
        when (action) {
            DoctorDetailsAction.BackToDoctorsList ->{}
        }
    }
}