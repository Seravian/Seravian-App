package com.seravian.auth.data.repository

import com.seravian.auth.domain.repository.RegisterRepository
import com.seravian.domain.datasource.RemoteDataSource

class RegisterRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
): RegisterRepository {
    override suspend fun registerUser(
        email: String,
        password: String,
        displayName: String
    ) {
        remoteDataSource.registerUser(email, password, displayName)
    }

    override suspend fun verifyUserRegistration(email: String, otp: String) {
        remoteDataSource.verifyUserRegistration(email, otp)
    }
}