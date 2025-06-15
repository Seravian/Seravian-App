package com.seravian.core_profile.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile")
    suspend fun getProfile(): ProfileEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profileEntity: ProfileEntity)

    @Update
    suspend fun updateProfile(profileEntity: ProfileEntity)

    @Query("DELETE FROM profile")
    suspend fun deleteProfile()
}