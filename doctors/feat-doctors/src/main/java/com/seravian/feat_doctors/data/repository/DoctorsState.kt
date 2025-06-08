package com.seravian.feat_doctors.data.repository

import com.seravian.feat_doctors.presentation.model.Doctor

data class DoctorsState(
    val currentDoctor: Doctor? = null
)
