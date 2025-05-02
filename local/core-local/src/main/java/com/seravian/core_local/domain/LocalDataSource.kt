package com.seravian.core_local.domain

import com.seravian.core_profile.data.local.ProfileEntity

interface LocalDataSource {
    suspend fun getProfile(): ProfileEntity
    suspend fun insertProfile(profileEntity: ProfileEntity)
    suspend fun updateProfile(profileEntity: ProfileEntity)
    suspend fun deleteProfile()
}