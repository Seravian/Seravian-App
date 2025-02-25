package com.seravian.onboarding.di

import com.seravian.onboarding.presentation.OnBoardingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val onBoardingModule = module {
    viewModel { OnBoardingViewModel(onBoardingRepository = get()) }
}