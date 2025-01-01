package com.seravian.auth.data.repository

import com.seravian.auth.domain.repository.ResetPasswordRepository
import com.seravian.domain.datasource.RemoteDataSource

class ResetPasswordRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
): ResetPasswordRepository {
    override suspend fun resetPassword(email: String, newPassword: String) {
        remoteDataSource.resetPassword(email, newPassword)
    }
}