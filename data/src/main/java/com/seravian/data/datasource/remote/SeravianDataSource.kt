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

    override suspend fun verifyUserRegistration(email: String, otp: String) {
        Log.d("SeravianDS", "Verification")
    }

    override suspend fun logoutUser() {
        Log.d("SeravianDS", "Logging Out")
    }
}