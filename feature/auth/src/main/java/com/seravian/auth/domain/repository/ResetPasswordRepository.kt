package com.seravian.auth.domain.repository

interface ResetPasswordRepository {
    suspend fun resetPassword(email: String, newPassword: String)
}