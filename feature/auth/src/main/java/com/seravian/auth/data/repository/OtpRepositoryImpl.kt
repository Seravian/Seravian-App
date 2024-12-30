package com.seravian.auth.data.repository

import com.seravian.auth.domain.repository.OtpRepository
import com.seravian.domain.datasource.RemoteDataSource

class OtpRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
): OtpRepository {
    override suspend fun verifyOtp(email: String, otp: String) {
        remoteDataSource.verifyOtp(email, otp)
    }
}