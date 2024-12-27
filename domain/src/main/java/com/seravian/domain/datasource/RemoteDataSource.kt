package com.seravian.domain.datasource

interface RemoteDataSource {
    suspend fun registerUser(username: String, email: String, password: String)
    suspend fun loginUser(email: String, password: String)
    suspend fun verifyOtp(email: String, otp: String)
    suspend fun logoutUser()
}