package com.greenvenom.feat_onboarding.di

import com.greenvenom.feat_onboarding.presentation.OnBoardingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val onBoardingFeatureModule = module {
    viewModel { OnBoardingViewModel(onBoardingRepository = get()) }
}