package com.seravian.feat_doctors.presentation.viewModel.doctor

import androidx.lifecycle.viewModelScope
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_ui.R
import com.greenvenom.core_ui.presentation.BaseAction
import com.greenvenom.core_ui.presentation.BaseViewModel
import com.seravian.core_doctors.domain.models.Doctor
import com.seravian.feat_doctors.data.repository.DoctorsStateRepository
import com.seravian.feat_doctors.domain.repository.DoctorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DoctorViewModel(
    private val doctorsStateRepository: DoctorsStateRepository,
    private val doctorRepository: DoctorRepository
) : BaseViewModel() {
    private val _doctorState: MutableStateFlow<DoctorState> = MutableStateFlow(DoctorState())
    val doctorState = _doctorState.asStateFlow()


    init {
        getAllDoctors()
    }

    fun getAllDoctors() {
        viewModelScope.launch {
            baseAction(BaseAction.ShowLoading)
            doctorRepository.getDoctors().onSuccess { result ->
                _doctorState.update {
                    it.copy(
                        doctors = result
                    )
                }
                baseAction(BaseAction.HideLoading)
            }
        }
    }

    fun doctorAction(action: DoctorAction) {
        when (action) {
            is DoctorAction.OnDoctorClick -> {
                doctorsStateRepository.updateCurrentDoctor(action.doctor.doctorId)
            }
        }
    }
}
