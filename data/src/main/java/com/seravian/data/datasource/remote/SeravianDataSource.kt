package com.seravian.data.datasource.remote

import android.util.Log
import com.seravian.domain.datasource.RemoteDataSource

class SeravianDataSource(): RemoteDataSource {
    override suspend fun registerUser(username: String, email: String, password: String) {
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

    override suspend fun logoutUser() {
        Log.d("SeravianDS", "Logging Out")
    }
}