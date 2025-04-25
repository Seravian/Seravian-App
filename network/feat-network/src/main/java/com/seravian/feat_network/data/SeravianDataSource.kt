package com.seravian.feat_network.data

import android.util.Log
import com.greenvenom.core_auth.data.dto.request.SendOTPRequest
import com.greenvenom.core_auth.data.dto.request.LoginRequest
import com.greenvenom.core_auth.data.dto.request.NewPasswordRequest
import com.greenvenom.core_auth.data.dto.request.VerifyOTPRequest
import com.greenvenom.core_auth.data.dto.request.RegisterRequest
import com.greenvenom.core_auth.data.dto.response.LoginResponse
import com.greenvenom.core_auth.data.dto.response.RegisterResponse
import com.greenvenom.core_network.api.utils.constructUrl
import com.greenvenom.core_network.api.utils.safeCall
import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_onboarding.data.dto.request.OnBoardingRequest
import com.greenvenom.core_onboarding.data.dto.response.OnBoardingResponse
import com.greenvenom.core_network.domain.repository.RemoteDataSource
import com.greenvenom.core_tokens.data.dto.request.RefreshTokenRequest
import com.greenvenom.core_tokens.data.dto.response.TokensResponse
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class SeravianDataSource(
    val publicHttpClient: HttpClient,
    val authorizedHttpClient: HttpClient
): RemoteDataSource {
    override suspend fun registerUser(
        registerRequest: RegisterRequest
    ): NetworkResult<RegisterResponse, NetworkError> {
        return safeCall {
            publicHttpClient.post(urlString = constructUrl("auth/register")) {
                setBody(registerRequest)
            }
        }
    }

    override suspend fun loginUser(
        loginRequest: LoginRequest
    ): NetworkResult<LoginResponse, NetworkError> {
        return safeCall {
            publicHttpClient.post(urlString = constructUrl("auth/login")) {
                setBody(loginRequest)
            }
        }
    }

    override suspend fun verifyOtp(
        verifyOtpRequest: VerifyOTPRequest
    ): EmptyResult<NetworkError> {
        return safeCall {
            publicHttpClient.post(urlString = constructUrl("auth/verify-otp")) {
                setBody(verifyOtpRequest)
            }
        }
    }

    override suspend fun sendOtp(
        sendOTPRequest: SendOTPRequest
    ): EmptyResult<NetworkError> {
        return safeCall {
            publicHttpClient.post(urlString = constructUrl("auth/resend-otp")) {
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

    override suspend fun updateUserDetails(
        onBoardingRequest: OnBoardingRequest
    ): NetworkResult<OnBoardingResponse, NetworkError> {
        return safeCall<OnBoardingResponse> {
            authorizedHttpClient.post(urlString = constructUrl("auth/complete-profile-setup")) {
                setBody(onBoardingRequest)
            }
        }.onSuccess { authorizedHttpClient.authProvider<BearerAuthProvider>()?.clearToken() }
    }

    override suspend fun refreshToken(
        refreshTokenRequest: RefreshTokenRequest
    ): NetworkResult<TokensResponse, NetworkError> {
        return safeCall {
            publicHttpClient.post(urlString = constructUrl("auth/refresh-token")) {
                setBody(refreshTokenRequest)
            }
        }
    }

    override suspend fun logoutUser(refreshToken: String): EmptyResult<NetworkError> {
        return safeCall<Unit> {
            authorizedHttpClient.post(urlString = constructUrl("auth/logout")) {
                setBody(
                    mapOf(
                        "refreshToken" to refreshToken
                    )
                )
            }
        }.onSuccess { authorizedHttpClient.authProvider<BearerAuthProvider>()?.clearToken() }
    }
}