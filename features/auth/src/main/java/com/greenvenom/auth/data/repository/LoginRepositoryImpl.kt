package com.greenvenom.auth.data.repository

import com.greenvenom.auth.domain.repository.LoginRepository
import com.greenvenom.networking.data.Result
import com.greenvenom.networking.domain.Error
import com.seravian.domain.datasource.RemoteDataSource

class LoginRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
): LoginRepository {
    override suspend fun loginUser(
        email: String,
        password: String
    ): Result<Any, Error> {
        return remoteDataSource.loginUser(email, password)
    }
}