package com.seravian.auth.di

import com.seravian.auth.data.repository.AuthStateRepository
import com.seravian.auth.data.repository.OtpRepositoryImpl
import com.seravian.auth.data.repository.LoginRepositoryImpl
import com.seravian.auth.data.repository.RegisterRepositoryImpl
import com.seravian.auth.data.repository.ResetPasswordRepositoryImpl
import com.seravian.auth.data.repository.UserDetailsRepositoryImpl
import com.seravian.auth.domain.repository.OtpRepository
import com.seravian.auth.domain.repository.LoginRepository
import com.seravian.auth.domain.repository.RegisterRepository
import com.seravian.auth.domain.repository.ResetPasswordRepository
import com.seravian.auth.domain.repository.UserDetailsRepository
import com.seravian.auth.presentation.login.LoginViewModel
import com.seravian.auth.presentation.otp.OtpViewModel
import com.seravian.auth.presentation.register.RegisterViewModel
import com.seravian.auth.presentation.reset_password.ResetPasswordViewModel
import com.seravian.auth.presentation.user_details.AuthUserDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authenticationModule = module {
    single { AuthStateRepository() }

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

    single<UserDetailsRepository> {
        UserDetailsRepositoryImpl(remoteDataSource = get())
    }

    viewModel { LoginViewModel(loginRepository = get()) }
    viewModel { RegisterViewModel(registerRepository = get()) }
    viewModel { OtpViewModel(authStateRepository = get(), otpRepository = get() ) }
    viewModel { ResetPasswordViewModel(authStateRepository = get(), resetPasswordRepository = get()) }
    viewModel { AuthUserDetailsViewModel(userDetailsRepository = get()) }
}