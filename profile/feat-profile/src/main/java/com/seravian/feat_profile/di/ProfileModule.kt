package com.seravian.feat_profile.di

import com.seravian.feat_profile.data.SeravianProfileDataSource
import com.seravian.feat_profile.data.repository.ProfileRepositoryImpl
import com.seravian.feat_profile.domain.ProfileRemoteDataSource
import com.seravian.feat_profile.domain.repository.ProfileRepository
import com.seravian.feat_profile.presentation.viewModel.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val profileModule = module {
    single<ProfileRemoteDataSource> {
        SeravianProfileDataSource(
            authorizedHttpClient = get(named("authorizedClient"))
        )
    }

    single<ProfileRepository> {
        ProfileRepositoryImpl(
            profileDataSource = get(),
            appPrefsDataSource = get(),
            roomDataSource = get(),
            tokensDataSource = get()
        )
    }

    viewModel {
        ProfileViewModel(
            profileRepository = get()
        )
    }
}