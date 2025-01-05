package com.seravian.auth.domain.repository

interface UserDetailsRepository {
    suspend fun updateUserDetails(
        fullName: String,
        userType: String,
        phoneNumber: String,
        birthDate: String,
        gender: String,
    )
}