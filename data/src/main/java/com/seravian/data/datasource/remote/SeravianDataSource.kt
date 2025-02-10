package com.seravian.data.datasource.remote

import android.util.Log
import com.greenvenom.networking.data.Result
import com.greenvenom.networking.domain.Error
import com.seravian.domain.datasource.RemoteDataSource
import io.ktor.client.HttpClient

class SeravianDataSource(
    val httpClient: HttpClient
): RemoteDataSource {
    override suspend fun registerUser(
        username: String,
        email: String,
        password: String
    ): Result<Any, Error> {
//        return safeCall<Unit> {
//            httpClient.get(
//                urlString = constructUrl("")
//            )
//        }

        Log.d("SeravianDS", "Registration")
        return Result.Success(Unit)
    }

    override suspend fun loginUser(email: String, password: String): Result<Any, Error> {
        Log.d("SeravianDS", "Logging In")
        return Result.Success(Unit)
    }

    override suspend fun verifyOtp(email: String, otp: String): Result<Any, Error> {
        Log.d("SeravianDS", "OTP Verification")
        return Result.Success(Unit)
    }

    override suspend fun sendResetPasswordEmail(email: String): Result<Any, Error> {
        Log.d("SeravianDS", "Sending Reset Password Email")
        return Result.Success(Unit)
    }

    override suspend fun updatePassword(password: String): Result<Any, Error> {
        Log.d("SeravianDS", "Updating Password")
        return Result.Success(Unit)
    }

    override suspend fun updateUserDetails(
        fullName: String,
        userType: String,
        phoneNumber: String,
        birthDate: String,
        gender: String
    ): Result<Any, Error> {
        Log.d("SeravianDS", "Updating User Details")
        return Result.Success(Unit)
    }

    override suspend fun logoutUser(): Result<Any, Error> {
        Log.d("SeravianDS", "Logging Out")
        return Result.Success(Unit)
    }
}