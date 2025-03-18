package com.seravian.feat_network.data.remote

import android.util.Log
import com.greenvenom.core_network.api.utils.constructUrl
import com.greenvenom.core_network.api.utils.safeCall
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.feat_network.domain.remote.RemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders

class SeravianDataSource(
    val publicHttpClient: HttpClient,
    val authorizedHttpClient: HttpClient
): RemoteDataSource {
    override suspend fun registerUser(
        username: String,
        email: String,
        password: String
    ): NetworkResult<Any, NetworkError> {
//        return safeCall<Unit> {
//            publicHttpClient.post(urlString = constructUrl("auth/register")) {
//                setBody(
//                    mapOf(
//                        "username" to username,
//                        "email" to email,
//                        "password" to password
//                    )
//                )
//            }
//        }

        Log.d("SeravianDS", "Registration")
        return NetworkResult.Success(Unit)
    }

    override suspend fun loginUser(email: String, password: String): NetworkResult<Any, NetworkError> {
//        return safeCall {
//            publicHttpClient.post(urlString = constructUrl("auth/login")) {
//                headers {
//                    append(HttpHeaders.AuthenticationInfo, "Bearer")
//                }
//                setBody(
//                    mapOf(
//                        "email" to email,
//                        "password" to password
//                    )
//                )
//            }
//        }

        Log.d("SeravianDS", "Logging In")
        return NetworkResult.Success(Unit)
    }

    override suspend fun verifyOtp(email: String, otp: String): NetworkResult<Any, NetworkError> {
        Log.d("SeravianDS", "OTP Verification")
        return NetworkResult.Success(Unit)
    }

    override suspend fun sendResetPasswordEmail(email: String): NetworkResult<Any, NetworkError> {
        Log.d("SeravianDS", "Sending Reset Password Email")
        return NetworkResult.Success(Unit)
    }

    override suspend fun updatePassword(password: String): NetworkResult<Any, NetworkError> {
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
        Log.d("SeravianDS", "Logging Out")
        return NetworkResult.Success(Unit)
    }
}