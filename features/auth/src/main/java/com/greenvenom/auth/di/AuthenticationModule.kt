package com.greenvenom.auth.di

import com.greenvenom.auth.data.repository.EmailStateRepository
import com.greenvenom.auth.data.repository.LoginRepositoryImpl
import com.greenvenom.auth.data.repository.OtpRepositoryImpl
import com.greenvenom.auth.data.repository.RegisterRepositoryImpl
import com.greenvenom.auth.data.repository.ResetPasswordRepositoryImpl
import com.greenvenom.auth.domain.repository.LoginRepository
import com.greenvenom.auth.domain.repository.OtpRepository
import com.greenvenom.auth.domain.repository.RegisterRepository
import com.greenvenom.auth.domain.repository.ResetPasswordRepository
import com.greenvenom.auth.presentation.login.LoginViewModel
import com.greenvenom.auth.presentation.otp.OtpViewModel
import com.greenvenom.auth.presentation.register.RegisterViewModel
import com.greenvenom.auth.presentation.reset_password.ResetPasswordViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authenticationModule = module {
    single { EmailStateRepository() }

    single<OtpRepository> {
        OtpRepositoryImpl(remoteDataSource = get())
    }

    single<LoginRepository> {
        LoginRepositoryImpl(remoteDataSource = get())
    }

    single<RegisterRepository> {
        RegisterRepositoryImpl(remoteDataSource = get())
    }

    single<ResetPasswordRepository> {
        ResetPasswordRepositoryImpl(remoteDataSource = get())
    }

    viewModel { LoginViewModel(loginRepository = get()) }
    viewModel { RegisterViewModel(emailStateRepository = get(), registerRepository = get()) }
    viewModel { OtpViewModel(emailStateRepository = get(), otpRepository = get() ) }
    viewModel { ResetPasswordViewModel(emailStateRepository = get(), resetPasswordRepository = get()) }
}