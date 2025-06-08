package com.seravian.feat_doctors.presentation.viewModel.doctor

import com.greenvenom.core_ui.R
import com.greenvenom.core_ui.presentation.BaseViewModel
import com.seravian.feat_doctors.data.repository.DoctorsStateRepository
import com.seravian.feat_doctors.presentation.model.Doctor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DoctorViewModel(
    private val doctorsStateRepository: DoctorsStateRepository
): BaseViewModel() {
    private val _doctorState:MutableStateFlow<DoctorState> = MutableStateFlow(DoctorState())
    val doctorState = _doctorState.asStateFlow()


    init {
        _doctorState.value = DoctorState(
            doctors = listOf(
                Doctor("1", R.drawable.logo, "Kareem Essam", "$20", "Mental Health Specialist"),
                Doctor("2", com.greenvenom.core_ui.R.drawable.logo, "Aya Ahmed", "$30", "CBT Therapist")
            )
        )
    }

    fun doctorAction(action: DoctorAction) {
        when (action) {
            is DoctorAction.OnDoctorClick -> {
                doctorsStateRepository.updateCurrentDoctor(action.doctor)
            }
        }
    }
}
