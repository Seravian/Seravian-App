package com.seravian.seravianapp.data.remote

import android.util.Log
import com.greenvenom.networking.data.NetworkError
import com.greenvenom.networking.data.NetworkResult
import com.seravian.seravianapp.domain.remote.RemoteDataSource
import io.ktor.client.HttpClient

class SeravianDataSource(
    val httpClient: HttpClient
): RemoteDataSource {
    override suspend fun registerUser(
        username: String,
        email: String,
        password: String
    ): NetworkResult<Any, NetworkError> {
//        return safeCall<Unit> {
//            httpClient.get(
//                urlString = constructUrl("")
//            )
//        }

        Log.d("SeravianDS", "Registration")
        return NetworkResult.Success(Unit)
    }

    override suspend fun loginUser(email: String, password: String): NetworkResult<Any, NetworkError> {
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