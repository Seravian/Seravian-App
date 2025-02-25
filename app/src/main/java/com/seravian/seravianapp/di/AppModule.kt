package com.seravian.seravianapp.di

import com.greenvenom.auth.domain.repository.AuthRepository
import com.seravian.seravianapp.data.features.onboarding.OnBoardingRepositoryImpl
import com.seravian.onboarding.domain.OnBoardingRepository
import com.seravian.seravianapp.data.features.auth.AuthRepositoryImpl
import com.seravian.seravianapp.data.remote.SeravianDataSource
import com.seravian.seravianapp.domain.remote.RemoteDataSource
import com.seravian.seravianapp.navigation.utils.SessionDestinationHandler
import org.koin.dsl.module

val appModule = module {
    single {
        SessionDestinationHandler(
            navigationStateRepository = get(),
            sessionStateRepository = get()
        )
    }

    single<AuthRepository> {
        AuthRepositoryImpl(remoteDataSource = get())
    }

    single<OnBoardingRepository> {
        OnBoardingRepositoryImpl(remoteDataSource = get())
    }

    single<RemoteDataSource>() {
        SeravianDataSource(
            httpClient = get()
        )
    }
}