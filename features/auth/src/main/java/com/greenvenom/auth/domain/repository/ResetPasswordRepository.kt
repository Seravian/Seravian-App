package com.greenvenom.auth.domain.repository

import com.greenvenom.networking.data.Result
import com.greenvenom.networking.domain.Error

interface ResetPasswordRepository {
    suspend fun sendResetPasswordEmail(email: String): Result<Any, Error>
    suspend fun updatePassword(newPassword: String): Result<Any, Error>
}