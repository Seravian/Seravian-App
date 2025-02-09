package com.seravian.data.datasource.remote

import android.util.Log
import com.greenvenom.networking.data.Result
import com.greenvenom.networking.domain.NetworkError
import com.greenvenom.networking.domain.datasource.RemoteDataSource
import io.ktor.client.HttpClient

class SeravianDataSource(
    val httpClient: HttpClient
): RemoteDataSource {
    override suspend fun registerUser(
        displayName: String,
        email: String,
        password: String
    ): Result<Any?, NetworkError> {
//        return safeCall<Unit> {
//            httpClient.get(
//                urlString = constructUrl("")
//            )
//        }

        Log.d("SeravianDS", "Registration")
        return Result.Success(null)
    }

    override suspend fun loginUser(email: String, password: String): Result<Any, NetworkError> {
        Log.d("SeravianDS", "Logging In")
        return Result.Success(Unit)
    }

    override suspend fun verifyOtp(email: String, otp: String): Result<Any, NetworkError> {
        Log.d("SeravianDS", "OTP Verification")
        return Result.Success(Unit)
    }

    override suspend fun sendResetPasswordEmail(email: String): Result<Any, NetworkError> {
        Log.d("SeravianDS", "Sending Reset Password Email")
        return Result.Success(Unit)
    }

    override suspend fun updatePassword(password: String): Result<Any, NetworkError> {
        Log.d("SeravianDS", "Updating Password")
        return Result.Success(Unit)
    }

    override suspend fun updateUserDetails(
        userType: String,
        phoneNumber: String,
        birthDate: String,
        gender: String
    ): Result<Any, NetworkError> {
        Log.d("SeravianDS", "Updating User Details")
        return Result.Success(Unit)
    }

    override suspend fun logoutUser(): Result<Any, NetworkError> {
        Log.d("SeravianDS", "Logging Out")
        return Result.Success(Unit)
    }
}