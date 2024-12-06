package com.seravian.seravianapp.features.auth.di

import com.seravian.seravianapp.features.auth.presentation.login.LoginViewModel
import com.seravian.seravianapp.features.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authenticationModule = module{
    viewModel { LoginViewModel() }
    viewModel { RegisterViewModel() }
}