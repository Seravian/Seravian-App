package com.seravian.auth.domain.repository

interface AccountRepository {
    suspend fun logoutUser()
}