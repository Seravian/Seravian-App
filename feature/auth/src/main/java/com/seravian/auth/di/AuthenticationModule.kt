package com.seravian.auth.di

import com.seravian.auth.data.repository.AccountRepositoryImpl
import com.seravian.auth.data.repository.LoginRepositoryImpl
import com.seravian.auth.data.repository.RegisterRepositoryImpl
import com.seravian.auth.domain.repository.AccountRepository
import com.seravian.auth.domain.repository.LoginRepository
import com.seravian.auth.domain.repository.RegisterRepository
import com.seravian.auth.presentation.AccountViewModel
import com.seravian.auth.presentation.login.LoginViewModel
import com.seravian.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authenticationModule = module{
    single<AccountRepository> {
        AccountRepositoryImpl(remoteDataSource = get())
    }

    single<LoginRepository> {
        LoginRepositoryImpl(remoteDataSource = get())
    }

    single<RegisterRepository> {
        RegisterRepositoryImpl(remoteDataSource = get())
    }

    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { AccountViewModel(get()) }
}