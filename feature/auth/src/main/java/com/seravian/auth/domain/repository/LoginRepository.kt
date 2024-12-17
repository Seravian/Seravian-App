package com.seravian.auth.domain.repository

interface LoginRepository {
    suspend fun loginUser(email: String, password: String)
}