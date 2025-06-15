package com.seravian.feat_auth.di

import com.seravian.feat_auth.data.SeravianAuthDataSource
import com.seravian.feat_auth.data.repository.EmailStateRepository
import com.seravian.feat_auth.domain.repository.AuthRepository
import com.seravian.feat_auth.data.repository.AuthRepositoryImpl
import com.seravian.feat_auth.domain.AuthRemoteDataSource
import com.seravian.feat_auth.presentation.login.LoginViewModel
import com.seravian.feat_auth.presentation.otp.OtpViewModel
import com.seravian.feat_auth.presentation.register.RegisterViewModel
import com.seravian.feat_auth.presentation.reset_password.ResetPasswordViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authModule = module {
    single { EmailStateRepository() }

    single<AuthRemoteDataSource> {
        SeravianAuthDataSource(
            publicHttpClient = get(named("publicClient")),
        )
    }

    single<AuthRepository> {
        AuthRepositoryImpl(
            authDataSource = get(),
            roomDataSource = get(),
            tokensDataSource = get(),
            emailStateRepository = get()
        )
    }

    viewModel { LoginViewModel(authRepository = get(), emailStateRepository = get()) }
    viewModel { RegisterViewModel(emailStateRepository = get(), authRepository = get()) }
    viewModel { OtpViewModel(emailStateRepository = get(), authRepository = get() ) }
    viewModel { ResetPasswordViewModel(emailStateRepository = get(), authRepository = get()) }
}