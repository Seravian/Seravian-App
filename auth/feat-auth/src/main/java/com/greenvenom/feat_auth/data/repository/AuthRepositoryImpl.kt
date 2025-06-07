package com.greenvenom.feat_auth.data.repository

import com.greenvenom.core_auth.data.dto.request.SendOTPRequest
import com.greenvenom.core_auth.data.dto.request.LoginRequest
import com.greenvenom.core_auth.data.dto.request.NewPasswordRequest
import com.greenvenom.core_auth.data.dto.request.VerifyOTPRequest
import com.greenvenom.core_auth.data.dto.request.RegisterRequest
import com.greenvenom.core_auth.data.dto.response.LoginResponse
import com.greenvenom.core_auth.data.dto.response.RegisterResponse
import com.greenvenom.feat_auth.domain.repository.AuthRepository
import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.onSuccess
import com.seravian.core_local.domain.LocalDataSource
import com.greenvenom.core_tokens.domain.Tokens
import com.greenvenom.core_tokens.domain.repo.TokensDataSource
import com.greenvenom.feat_auth.domain.AuthRemoteDataSource

class AuthRepositoryImpl(
    private val authDataSource: AuthRemoteDataSource,
    private val roomDataSource: LocalDataSource,
    private val tokensDataSource: TokensDataSource,
    private val emailStateRepository: EmailStateRepository
): AuthRepository {
    override suspend fun loginUser(
        loginRequest: LoginRequest
    ): NetworkResult<LoginResponse, NetworkError> {
        val loginResponse = authDataSource.loginUser(loginRequest)
        return loginResponse.onSuccess { response ->
            if (response.isEmailVerified) {
                roomDataSource.insertProfile(response.extractProfile().toProfileEntity())
                tokensDataSource.saveTokensLocally(response.tokens?.extractTokens() as Tokens)
            }
        }
    }

    override suspend fun registerUser(
        registerRequest: RegisterRequest
    ): NetworkResult<RegisterResponse, NetworkError> {
        val registerResponse = authDataSource.registerUser(registerRequest)
        return registerResponse.onSuccess { response ->
            emailStateRepository.updateEmail(response.email)
        }
    }

    override suspend fun sendOTP(
        sendOTPRequest: SendOTPRequest
    ): EmptyResult<NetworkError> {
        return authDataSource.sendOtp(sendOTPRequest)
    }

    override suspend fun updatePassword(
        newPasswordRequest: NewPasswordRequest
    ): NetworkResult<Any, NetworkError> {
        return authDataSource.updatePassword(newPasswordRequest)
    }

    override suspend fun verifyOTP(
        verifyOtpRequest: VerifyOTPRequest
    ): EmptyResult<NetworkError> {
        return authDataSource.verifyOtp(verifyOtpRequest)
    }
}