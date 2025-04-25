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
import com.greenvenom.core_network.domain.repository.RemoteDataSource
import com.greenvenom.core_network.domain.repository.TokensRepository
import com.greenvenom.core_tokens.domain.Tokens

class AuthRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val roomDataSource: LocalDataSource,
    private val tokensRepository: TokensRepository,
    private val emailStateRepository: EmailStateRepository
): AuthRepository {
    override suspend fun loginUser(
        loginRequest: LoginRequest
    ): NetworkResult<LoginResponse, NetworkError> {
        val loginResponse = remoteDataSource.loginUser(loginRequest)
        return loginResponse.onSuccess { response ->
            if (response.isEmailVerified) {
                roomDataSource.insertProfile(response.extractProfile().toProfileEntity())
                tokensRepository.saveTokensLocally(response.tokens?.extractTokens() as Tokens)
            }
        }
    }

    override suspend fun registerUser(
        registerRequest: RegisterRequest
    ): NetworkResult<RegisterResponse, NetworkError> {
        val registerResponse = remoteDataSource.registerUser(registerRequest)
        return registerResponse.onSuccess { response ->
            emailStateRepository.updateEmail(response.email)
        }
    }

    override suspend fun sendOTP(
        sendOTPRequest: SendOTPRequest
    ): EmptyResult<NetworkError> {
        return remoteDataSource.sendOtp(sendOTPRequest)
    }

    override suspend fun updatePassword(
        newPasswordRequest: NewPasswordRequest
    ): NetworkResult<Any, NetworkError> {
        return remoteDataSource.updatePassword(newPasswordRequest)
    }

    override suspend fun verifyOTP(
        verifyOtpRequest: VerifyOTPRequest
    ): EmptyResult<NetworkError> {
        return remoteDataSource.verifyOtp(verifyOtpRequest)
    }
}