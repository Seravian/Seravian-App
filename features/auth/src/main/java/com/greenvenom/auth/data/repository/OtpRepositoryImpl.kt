package com.greenvenom.auth.data.repository

import com.greenvenom.auth.domain.repository.OtpRepository
import com.greenvenom.networking.data.Result
import com.greenvenom.networking.domain.Error
import com.seravian.domain.datasource.RemoteDataSource

class OtpRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
): OtpRepository {
    override suspend fun verifyOtp(
        email: String,
        otp: String
    ): Result<Any, Error> {
        return remoteDataSource.verifyOtp(email, otp)
    }
}