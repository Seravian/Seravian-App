package com.greenvenom.feat_onboarding.di

import com.greenvenom.feat_onboarding.domain.OnBoardingRepository
import com.greenvenom.feat_onboarding.data.OnBoardingRepositoryImpl
import com.greenvenom.feat_onboarding.presentation.OnBoardingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val onBoardingFeatureModule = module {
    single<OnBoardingRepository> {
        OnBoardingRepositoryImpl(
            remoteDataSource = get(),
            roomDataSource = get(),
            tokensRepository = get()
        )
    }

    viewModel { OnBoardingViewModel(onBoardingRepository = get()) }
}