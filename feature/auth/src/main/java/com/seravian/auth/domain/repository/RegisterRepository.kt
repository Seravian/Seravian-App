package com.seravian.auth.domain.repository

interface RegisterRepository {
    suspend fun registerUser(email: String, password: String, displayName: String)
    suspend fun verifyUserRegistration(email: String, otp: String)
}