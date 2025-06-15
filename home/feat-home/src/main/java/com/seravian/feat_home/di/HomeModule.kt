package com.seravian.feat_home.di

import com.seravian.feat_home.data.HomeRepositoryImpl
import com.seravian.feat_home.data.SeravianHomeDataSource
import com.seravian.feat_home.domain.HomeDataSource
import com.seravian.feat_home.domain.HomeRepository
import com.seravian.feat_home.presentation.viewmodel.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val homeModule = module {
    single<HomeDataSource> {
        SeravianHomeDataSource(
            authorizedHttpClient = get(named("authorizedClient"))
        )
    }

    single<HomeRepository> {
        HomeRepositoryImpl(
            homeDataSource = get()
        )
    }

    viewModel {
        HomeViewModel(
            homeRepository = get()
        )
    }
}