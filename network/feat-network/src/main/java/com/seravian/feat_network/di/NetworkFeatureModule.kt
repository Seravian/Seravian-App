package com.seravian.feat_network.di

import com.greenvenom.core_auth.domain.repository.AuthRepository
import com.greenvenom.core_onboarding.domain.OnBoardingRepository
import com.seravian.feat_network.data.features.auth.AuthRepositoryImpl
import com.seravian.feat_network.data.features.onboarding.OnBoardingRepositoryImpl
import com.seravian.feat_network.data.remote.SeravianDataSource
import com.seravian.feat_network.domain.remote.RemoteDataSource
import org.koin.dsl.module

val networkFeatureModule = module {
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