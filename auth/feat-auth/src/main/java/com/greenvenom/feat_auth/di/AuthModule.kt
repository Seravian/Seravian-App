package com.greenvenom.feat_auth.di

import com.greenvenom.feat_auth.data.repository.EmailStateRepository
import com.greenvenom.feat_auth.domain.repository.AuthRepository
import com.greenvenom.feat_auth.data.repository.AuthRepositoryImpl
import com.greenvenom.feat_auth.presentation.login.LoginViewModel
import com.greenvenom.feat_auth.presentation.otp.OtpViewModel
import com.greenvenom.feat_auth.presentation.register.RegisterViewModel
import com.greenvenom.feat_auth.presentation.reset_password.ResetPasswordViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single { EmailStateRepository() }

    single<AuthRepository> {
        AuthRepositoryImpl(
            remoteDataSource = get(),
            roomDataSource = get(),
            tokensRepository = get(),
            emailStateRepository = get()
        )
    }

    viewModel { LoginViewModel(authRepository = get(), emailStateRepository = get()) }
    viewModel { RegisterViewModel(emailStateRepository = get(), authRepository = get()) }
    viewModel { OtpViewModel(emailStateRepository = get(), authRepository = get() ) }
    viewModel { ResetPasswordViewModel(emailStateRepository = get(), authRepository = get()) }
}