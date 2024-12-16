package com.seravian.auth.di

import com.seravian.auth.presentation.login.LoginViewModel
import com.seravian.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authenticationModule = module{
    viewModel { LoginViewModel() }
    viewModel { RegisterViewModel() }
}