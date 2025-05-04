package com.seravian.feat_local.data

import com.seravian.core_chat.data.entity.ChatDao
import com.seravian.core_local.domain.LocalDataSource
import com.seravian.core_profile.data.local.ProfileDao
import com.seravian.core_profile.data.local.ProfileEntity

class RoomDataSource(
    private val profileDao: ProfileDao,
    private val chatDao: ChatDao
): LocalDataSource {
    override suspend fun getProfile(): ProfileEntity {
        return profileDao.getProfile()
    }

    override suspend fun insertProfile(profileEntity: ProfileEntity) {
        profileDao.insertProfile(profileEntity)
    }

    override suspend fun updateProfile(profileEntity: ProfileEntity) {
        profileDao.updateProfile(profileEntity)
    }

    override suspend fun deleteProfile() {
        profileDao.deleteProfile()
    }
}