package com.seravian.seravianapp.di

import com.seravian.seravianapp.core.navigation.AppNavigator
import com.seravian.seravianapp.splash.presentation.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { SplashViewModel() }
}