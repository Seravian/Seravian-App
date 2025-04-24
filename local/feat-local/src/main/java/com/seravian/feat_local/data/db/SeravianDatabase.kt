package com.seravian.feat_local.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.seravian.core_profile.data.ProfileDao
import com.seravian.core_profile.data.ProfileEntity

@Database(
    entities = [ProfileEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SeravianDatabase: RoomDatabase() {
    abstract val profileDao: ProfileDao
}