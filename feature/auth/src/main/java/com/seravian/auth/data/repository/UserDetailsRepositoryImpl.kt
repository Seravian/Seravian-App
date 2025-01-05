package com.seravian.auth.data.repository

import com.seravian.auth.domain.repository.UserDetailsRepository
import com.seravian.domain.datasource.RemoteDataSource

class UserDetailsRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
): UserDetailsRepository {
    override suspend fun updateUserDetails(
        fullName: String,
        userType: String,
        phoneNumber: String,
        birthDate: String,
        gender: String
    ) {
        remoteDataSource.updateUserDetails(
            fullName = fullName,
            userType = userType,
            phoneNumber = phoneNumber,
            birthDate = birthDate,
            gender = gender
        )
    }
}