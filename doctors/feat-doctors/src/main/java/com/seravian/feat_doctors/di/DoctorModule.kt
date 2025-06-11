package com.seravian.feat_doctors.di

import com.seravian.feat_doctors.data.SeravianDoctorDataSource
import com.seravian.feat_doctors.data.repository.DoctorListRepositoryImpl
import com.seravian.feat_doctors.data.repository.DoctorsStateRepository
import com.seravian.feat_doctors.domain.DoctorRemoteDataSource
import com.seravian.feat_doctors.domain.repository.DoctorRepository
import com.seravian.feat_doctors.presentation.viewModel.detailsDoctor.DoctorDetailsViewModel
import com.seravian.feat_doctors.presentation.viewModel.doctor.DoctorViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named


val doctorModule = module {

    single <DoctorRemoteDataSource>{
        SeravianDoctorDataSource(
            authorizedHttpClient = get(named("authorizedClient"))
        )
    }

    single<DoctorRepository>{
        DoctorListRepositoryImpl(
            seravianDoctorDataSource = get()
        )
    }

    single {
        DoctorsStateRepository()
    }

    viewModel {
        DoctorViewModel(
            doctorsStateRepository = get(),
            doctorRepository = get()
        )
    }
    viewModel {
        DoctorDetailsViewModel(
            doctorRepository = get(),
            doctorsStateRepository = get())
    }
}