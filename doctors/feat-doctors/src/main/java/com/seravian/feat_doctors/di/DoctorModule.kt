package com.seravian.feat_doctors.di

import com.seravian.feat_doctors.data.dataSource.CreateSessionDataSource
import com.seravian.feat_doctors.data.dataSource.SeravianDoctorDataSource
import com.seravian.feat_doctors.data.repository.CreateSessionRepositoryImpl
import com.seravian.feat_doctors.data.repository.DoctorListRepositoryImpl
import com.seravian.feat_doctors.data.repository.DoctorsStateRepository
import com.seravian.feat_doctors.domain.remoteDataSource.CreateSessionRemoteDataSource
import com.seravian.feat_doctors.domain.remoteDataSource.DoctorRemoteDataSource
import com.seravian.feat_doctors.domain.repository.CreateSessionRepository
import com.seravian.feat_doctors.domain.repository.DoctorRepository
import com.seravian.feat_doctors.presentation.viewModel.detailsDoctor.DoctorDetailsViewModel
import com.seravian.feat_doctors.presentation.viewModel.doctor.DoctorViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named


val doctorModule = module {

    single<CreateSessionRepository>{
        CreateSessionRepositoryImpl(
            createSessionDataSource = get()
        )
    }

    single<CreateSessionRemoteDataSource> {
        CreateSessionDataSource(
            authorizedHttpClient = get(named("authorizedClient"))
        )
    }

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
            doctorsStateRepository = get(),
            createSessionRepository = get())
    }
}