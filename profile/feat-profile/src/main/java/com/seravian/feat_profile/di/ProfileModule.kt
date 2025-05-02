package com.seravian.feat_profile.di

import com.seravian.feat_profile.data.ProfileRepositoryImpl
import com.seravian.feat_profile.domain.ProfileRepository
import com.seravian.feat_profile.presentation.viewModel.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    single<ProfileRepository> {
        ProfileRepositoryImpl(
            appPrefsDataSource = get(),
            roomDataSource = get(),
            seravianDataSource = get(),
            tokensDataSource = get()
        )
    }

    viewModel {
        ProfileViewModel(
            profileRepository = get()
        )
    }
}