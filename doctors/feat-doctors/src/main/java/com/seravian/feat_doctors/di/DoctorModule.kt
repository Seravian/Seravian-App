package com.seravian.feat_doctors.di

import com.seravian.feat_doctors.data.repository.DoctorsStateRepository
import com.seravian.feat_doctors.presentation.viewModel.detailsDoctor.DoctorDetailsViewModel
import com.seravian.feat_doctors.presentation.viewModel.doctor.DoctorViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel


val doctorModule = module {
    single {
        DoctorsStateRepository()
    }

    viewModel {
        DoctorViewModel(get())
    }
    viewModel {
        DoctorDetailsViewModel(get())
    }
}