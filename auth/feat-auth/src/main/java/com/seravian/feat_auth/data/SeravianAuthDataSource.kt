package com.seravian.feat_auth.data

import android.util.Log
import com.seravian.core_auth.data.dto.request.LoginRequest
import com.seravian.core_auth.data.dto.request.NewPasswordRequest
import com.seravian.core_auth.data.dto.request.RegisterRequest
import com.seravian.core_auth.data.dto.request.SendOTPRequest
import com.seravian.core_auth.data.dto.request.VerifyOTPRequest
import com.seravian.core_auth.data.dto.response.LoginResponse
import com.seravian.core_auth.data.dto.response.RegisterResponse
import com.seravian.core_network.api.utils.constructUrl
import com.seravian.core_network.api.utils.safeCall
import com.seravian.core_network.data.EmptyResult
import com.seravian.core_network.data.NetworkError
import com.seravian.core_network.data.NetworkResult
import com.seravian.feat_auth.domain.AuthRemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class SeravianAuthDataSource(
    private val publicHttpClient: HttpClient
): AuthRemoteDataSource {
    override suspend fun registerUser(
        registerRequest: RegisterRequest
    ): NetworkResult<RegisterResponse, NetworkError> {
        return safeCall {
            publicHttpClient.post(constructUrl("auth/register")) {
                setBody(registerRequest)
            }
        }
    }

    override suspend fun loginUser(
        loginRequest: LoginRequest
    ): NetworkResult<LoginResponse, NetworkError> {
        return safeCall {
            publicHttpClient.post(constructUrl("auth/login")) {
                setBody(loginRequest)
            }
        }
    }

    override suspend fun verifyOtp(
        verifyOtpRequest: VerifyOTPRequest
    ): EmptyResult<NetworkError> {
        return safeCall {
            publicHttpClient.post(constructUrl("auth/verify-otp")) {
                setBody(verifyOtpRequest)
            }
        }
    }

    override suspend fun sendOtp(
        sendOTPRequest: SendOTPRequest
    ): EmptyResult<NetworkError> {
        return safeCall {
            publicHttpClient.post(constructUrl("auth/resend-otp")) {
                setBody(
                    mapOf(
                        "email" to sendOTPRequest.email
                    )
                )
            }
        }
    }

    override suspend fun updatePassword(
        newPasswordRequest: NewPasswordRequest
    ): NetworkResult<Any, NetworkError> {
        Log.d("SeravianDS", "Updating Password")
        return NetworkResult.Success(Unit)
    }
}