package com.seravian.feat_onboarding.di

import com.seravian.feat_onboarding.data.SeravianOnBoardingDataSource
import com.seravian.feat_onboarding.domain.repository.OnBoardingRepository
import com.seravian.feat_onboarding.data.repository.OnBoardingRepositoryImpl
import com.seravian.feat_onboarding.domain.OnBoardingRemoteDataSource
import com.seravian.feat_onboarding.presentation.OnBoardingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val onBoardingModule = module {
    single<OnBoardingRemoteDataSource> {
        SeravianOnBoardingDataSource(
            authorizedHttpClient = get(named("authorizedClient"))
        )
    }

    single<OnBoardingRepository> {
        OnBoardingRepositoryImpl(
            onBoardingDataSource = get(),
            roomDataSource = get(),
            tokensDataSource = get()
        )
    }

    viewModel { OnBoardingViewModel(onBoardingRepository = get()) }
}