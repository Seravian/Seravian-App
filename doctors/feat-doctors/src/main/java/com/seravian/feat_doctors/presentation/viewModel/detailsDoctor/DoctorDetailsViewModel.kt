package com.seravian.feat_doctors.presentation.viewModel.detailsDoctor

import androidx.lifecycle.viewModelScope
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_ui.presentation.BaseAction
import com.greenvenom.core_ui.presentation.BaseViewModel
import com.seravian.core_doctors.data.dto.request.CreateSessionRequest
import com.seravian.core_doctors.data.dto.request.GetDoctorRequest
import com.seravian.feat_doctors.data.repository.DoctorsStateRepository
import com.seravian.feat_doctors.domain.repository.CreateSessionRepository
import com.seravian.feat_doctors.domain.repository.DoctorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DoctorDetailsViewModel(
    private val doctorsStateRepository: DoctorsStateRepository,
    private val doctorRepository: DoctorRepository,
    private val createSessionRepository: CreateSessionRepository
):BaseViewModel() {
    private val _doctorDetailsState = MutableStateFlow(DoctorDetailsState())
    val doctorDetailsState = _doctorDetailsState.asStateFlow()


    init {
        getDoctorDetails()
    }

    private fun createSession(
        patientIsAvailableFromUtc: String,
        patientIsAvailableToUtc: String,
        patientNote: String
    ){
        viewModelScope.launch {
            baseAction(BaseAction.ShowLoading)
            createSessionRepository.createSession(
                createSessionRequest = CreateSessionRequest(
                    doctorId = doctorsStateRepository.doctorsState.value.currentDoctor?:"",
                    patientIsAvailableToUtc = patientIsAvailableToUtc,
                    patientIsAvailableFromUtc = patientIsAvailableFromUtc,
                    patientNote = patientNote
                    )
            )
            baseAction(BaseAction.HideLoading)
        }
    }

    private fun getDoctorDetails(){
        viewModelScope.launch {
            baseAction(BaseAction.ShowLoading)
            doctorRepository.getDoctorsDetails(
                getDoctorRequest = GetDoctorRequest(doctorsStateRepository.doctorsState.value.currentDoctor?:"")
            ).onSuccess { result ->
                _doctorDetailsState.update {
                    it.copy(
                        doctor = result
                    )
                }
            }
            baseAction(BaseAction.HideLoading)
        }
    }

    fun doctorDetailsAction(action: DoctorDetailsAction) {
        when (action) {
            DoctorDetailsAction.BackToDoctorsList ->{}
            is DoctorDetailsAction.BookSession -> {
                createSession(
                    patientIsAvailableToUtc = action.patientIsAvailableToUtc,
                    patientIsAvailableFromUtc = action.patientIsAvailableFromUtc,
                    patientNote = action.patientNote
                )
            }
        }
    }
}