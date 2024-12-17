package com.seravian.auth.data.repository

import com.seravian.auth.domain.repository.AccountRepository
import com.seravian.domain.datasource.RemoteDataSource

class AccountRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
): AccountRepository {
    override suspend fun logoutUser() {
        remoteDataSource.logoutUser()
    }
}