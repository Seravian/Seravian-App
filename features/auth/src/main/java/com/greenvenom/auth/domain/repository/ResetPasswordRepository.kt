package com.greenvenom.auth.domain.repository

import com.greenvenom.networking.data.Result
import com.greenvenom.networking.domain.NetworkError

interface ResetPasswordRepository {
    suspend fun sendResetPasswordEmail(email: String): Result<Any, NetworkError>
    suspend fun updatePassword(newPassword: String): Result<Any, NetworkError>
}