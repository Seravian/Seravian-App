package com.seravian.data.datasource.remote

import android.util.Log
import com.greenvenom.networking.domain.constructUrl
import com.greenvenom.networking.domain.safeCall
import com.seravian.domain.datasource.RemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class SeravianDataSource(
    val httpClient: HttpClient
): RemoteDataSource {
    override suspend fun registerUser(username: String, email: String, password: String) {
//        return safeCall<Unit> {
//            httpClient.get(
//                urlString = constructUrl("")
//            )
//        }

        Log.d("SeravianDS", "Registration")
    }

    override suspend fun loginUser(email: String, password: String) {
        Log.d("SeravianDS", "Logging In")
    }

    override suspend fun verifyOtp(email: String, otp: String) {
        Log.d("SeravianDS", "OTP Verification")
    }

    override suspend fun resetPassword(email: String, newPassword: String) {
        Log.d("SeravianDS", "New Password")
    }

    override suspend fun updateUserDetails(
        fullName: String,
        userType: String,
        phoneNumber: String,
        birthDate: String,
        gender: String
    ) {
        Log.d("SeravianDS", "Updating User Details")
    }

    override suspend fun logoutUser() {
        Log.d("SeravianDS", "Logging Out")
    }
}