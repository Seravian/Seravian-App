package com.seravian.auth.data.repository

import com.seravian.auth.domain.repository.AccountRepository
import com.seravian.domain.datasource.RemoteDataSource

class AccountRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
): AccountRepository {
    override suspend fun verifyOtp(email: String, otp: String) {
        remoteDataSource.verifyOtp(email, otp)
    }

    override suspend fun logoutUser() {
        remoteDataSource.logoutUser()
    }
}