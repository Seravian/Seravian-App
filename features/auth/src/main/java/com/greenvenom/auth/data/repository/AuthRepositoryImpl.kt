package com.greenvenom.auth.data.repository

import com.greenvenom.auth.domain.repository.AuthRepository
import com.greenvenom.networking.data.Result
import com.greenvenom.networking.domain.Error
import com.seravian.domain.datasource.RemoteDataSource

class AuthRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
): AuthRepository {
    override suspend fun loginUser(
        email: String,
        password: String
    ): Result<Any, Error> {
        return remoteDataSource.loginUser(email, password)
    }

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

    override suspend fun sendResetPasswordEmail(email: String): Result<Any, Error> {
        return remoteDataSource.sendResetPasswordEmail(email)
    }

    override suspend fun updatePassword(newPassword: String): Result<Any, Error> {
        return remoteDataSource.updatePassword(password = newPassword)
    }

    override suspend fun verifyOtp(
        email: String,
        otp: String
    ): Result<Any, Error> {
        return remoteDataSource.verifyOtp(email, otp)
    }
}