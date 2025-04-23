package com.seravian.feat_network.data.features.auth

import com.greenvenom.core_auth.data.dto.request.ResetPasswordRequest
import com.greenvenom.core_auth.data.dto.request.LoginRequest
import com.greenvenom.core_auth.data.dto.request.NewPasswordRequest
import com.greenvenom.core_auth.data.dto.request.OTPRequest
import com.greenvenom.core_auth.data.dto.request.RegisterRequest
import com.greenvenom.core_auth.data.dto.response.LoginResponse
import com.greenvenom.core_auth.data.dto.response.RegisterResponse
import com.greenvenom.core_auth.domain.repository.AuthRepository
import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.feat_network.domain.RemoteDataSource

class AuthRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
): AuthRepository {
    override suspend fun loginUser(
        loginRequest: LoginRequest
    ): NetworkResult<LoginResponse, NetworkError> {
        return remoteDataSource.loginUser(loginRequest)
    }

    override suspend fun registerUser(
        registerRequest: RegisterRequest
    ): NetworkResult<RegisterResponse, NetworkError> {
        return remoteDataSource.registerUser(registerRequest)
    }

    override suspend fun sendResetPasswordEmail(
        resetPasswordRequest: ResetPasswordRequest
    ): NetworkResult<Any, NetworkError> {
        return remoteDataSource.sendResetPasswordEmail(resetPasswordRequest)
    }

    override suspend fun updatePassword(
        newPasswordRequest: NewPasswordRequest
    ): NetworkResult<Any, NetworkError> {
        return remoteDataSource.updatePassword(newPasswordRequest)
    }

    override suspend fun verifyOtp(
        otpRequest: OTPRequest
    ): EmptyResult<NetworkError> {
        return remoteDataSource.verifyOtp(otpRequest)
    }
}