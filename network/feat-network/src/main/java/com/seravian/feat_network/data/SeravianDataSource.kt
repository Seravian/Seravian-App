package com.seravian.feat_network.data

import android.util.Log
import com.greenvenom.core_auth.data.dto.request.ResetPasswordRequest
import com.greenvenom.core_auth.data.dto.request.LoginRequest
import com.greenvenom.core_auth.data.dto.request.NewPasswordRequest
import com.greenvenom.core_auth.data.dto.request.OTPRequest
import com.greenvenom.core_auth.data.dto.request.RegisterRequest
import com.greenvenom.core_auth.data.dto.response.LoginResponse
import com.greenvenom.core_auth.data.dto.response.RegisterResponse
import com.greenvenom.core_auth.data.dto.response.TokensResponse
import com.greenvenom.core_network.api.utils.constructUrl
import com.greenvenom.core_network.api.utils.safeCall
import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_local.data.TokensInfo
import com.seravian.feat_network.domain.RemoteDataSource
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
        otpRequest: OTPRequest
    ): EmptyResult<NetworkError> {
        return safeCall {
            publicHttpClient.post(urlString = constructUrl("auth/verify-otp")) {
                setBody(otpRequest)
            }
        }
    }

    override suspend fun sendResetPasswordEmail(
        resetPasswordRequest: ResetPasswordRequest
    ): NetworkResult<Any, NetworkError> {
        Log.d("SeravianDS", "Sending Reset Password Email")
        return NetworkResult.Success(Unit)
    }

    override suspend fun updatePassword(
        newPasswordRequest: NewPasswordRequest
    ): NetworkResult<Any, NetworkError> {
        Log.d("SeravianDS", "Updating Password")
        return NetworkResult.Success(Unit)
    }

    override suspend fun updateUserDetails(
        fullName: String,
        userType: String,
        phoneNumber: String,
        birthDate: String,
        gender: String
    ): NetworkResult<Any, NetworkError> {
        Log.d("SeravianDS", "Updating User Details")
        return NetworkResult.Success(Unit)
    }

    override suspend fun logoutUser(): NetworkResult<Any, NetworkError> {
        authorizedHttpClient.authProvider<BearerAuthProvider>()?.clearToken()
        Log.d("SeravianDS", "Logging Out")
        return NetworkResult.Success(Unit)
    }

    override suspend fun refreshToken(refreshToken: String): NetworkResult<TokensInfo, NetworkError> {
        return safeCall {
            publicHttpClient.post(urlString = constructUrl("auth/refresh-token")) {
                setBody(
                    mapOf(
                        "refreshToken" to refreshToken
                    )
                )
            }
        }
    }
}