package com.seravian.auth.data.repository

import com.seravian.auth.domain.repository.LoginRepository
import com.seravian.domain.datasource.RemoteDataSource

class LoginRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
): LoginRepository {
    override suspend fun loginUser(email: String, password: String) {
        remoteDataSource.loginUser(email, password)
    }
}