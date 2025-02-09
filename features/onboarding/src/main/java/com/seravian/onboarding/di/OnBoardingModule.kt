package com.seravian.onboarding.di

import com.seravian.onboarding.data.OnBoardingRepositoryImpl
import com.seravian.onboarding.domain.OnBoardingRepository
import com.seravian.onboarding.presentation.OnBoardingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val onBoardingModule = module {
    single<OnBoardingRepository> {
        OnBoardingRepositoryImpl(remoteDataSource = get())
    }

    viewModel { OnBoardingViewModel(onBoardingRepository = get()) }
}