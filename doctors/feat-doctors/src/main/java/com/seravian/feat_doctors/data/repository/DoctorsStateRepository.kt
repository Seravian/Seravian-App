package com.seravian.feat_doctors.data.repository

import com.seravian.feat_doctors.presentation.model.Doctor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DoctorsStateRepository() {
    private val _doctorsState = MutableStateFlow(DoctorsState())
    val doctorsState = _doctorsState.asStateFlow()

    fun updateCurrentDoctor(doctor: Doctor) {
        _doctorsState.update {
            it.copy(
                currentDoctor = doctor
            )
        }
    }
}