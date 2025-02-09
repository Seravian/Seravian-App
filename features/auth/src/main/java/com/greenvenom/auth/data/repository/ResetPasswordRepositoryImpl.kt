package com.greenvenom.auth.data.repository

import com.greenvenom.auth.domain.repository.ResetPasswordRepository
import com.greenvenom.networking.data.Result
import com.greenvenom.networking.domain.NetworkError
import com.greenvenom.networking.domain.datasource.RemoteDataSource

class ResetPasswordRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
): ResetPasswordRepository {
    override suspend fun sendResetPasswordEmail(email: String): Result<Any, NetworkError> {
        return remoteDataSource.sendResetPasswordEmail(email)
    }

    override suspend fun updatePassword(newPassword: String): Result<Any, NetworkError> {
        return remoteDataSource.updatePassword(password = newPassword)
    }
}