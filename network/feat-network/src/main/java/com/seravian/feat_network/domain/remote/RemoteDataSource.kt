package com.seravian.feat_network.domain.remote

import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult

interface RemoteDataSource {
    suspend fun registerUser(username: String, email: String, password: String): NetworkResult<Any, NetworkError>
    suspend fun loginUser(email: String, password: String): NetworkResult<Any, NetworkError>
    suspend fun verifyOtp(email: String, otp: String): NetworkResult<Any, NetworkError>
    suspend fun sendResetPasswordEmail(email: String): NetworkResult<Any, NetworkError>
    suspend fun updatePassword(password: String): NetworkResult<Any, NetworkError>
    suspend fun updateUserDetails(
        fullName: String,
        userType: String,
        phoneNumber: String,
        birthDate: String,
        gender: String
    ): NetworkResult<Any, NetworkError>
    suspend fun logoutUser(): NetworkResult<Any, NetworkError>
}