package com.greenvenom.auth.data.repository

import com.greenvenom.auth.domain.repository.RegisterRepository
import com.greenvenom.networking.data.Result
import com.greenvenom.networking.domain.Error
import com.seravian.domain.datasource.RemoteDataSource

class RegisterRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
): RegisterRepository {
    override suspend fun registerUser(
        email: String,
        password: String,
        displayName: String
    ): Result<Any?, Error> {
        return remoteDataSource.registerUser(email, password, displayName)
    }

    override suspend fun verifyUserRegistration(
        email: String,
        otp: String
    ): Result<Any, Error> {
        return remoteDataSource.verifyOtp(email, otp)
    }
}