package com.greenvenom.auth.domain.repository

import com.greenvenom.networking.data.Result
import com.greenvenom.networking.domain.Error

interface LoginRepository {
    suspend fun loginUser(email: String, password: String): Result<Any, Error>
}