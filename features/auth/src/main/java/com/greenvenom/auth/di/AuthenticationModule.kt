package com.greenvenom.auth.di

import com.greenvenom.auth.data.EmailStateRepository
import com.greenvenom.auth.presentation.login.LoginViewModel
import com.greenvenom.auth.presentation.otp.OtpViewModel
import com.greenvenom.auth.presentation.register.RegisterViewModel
import com.greenvenom.auth.presentation.reset_password.ResetPasswordViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authenticationModule = module {
    single { EmailStateRepository() }

    viewModel { LoginViewModel(authRepository = get()) }
    viewModel { RegisterViewModel(emailStateRepository = get(), authRepository = get()) }
    viewModel { OtpViewModel(emailStateRepository = get(), authRepository = get() ) }
    viewModel { ResetPasswordViewModel(emailStateRepository = get(), authRepository = get()) }
}