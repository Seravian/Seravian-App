package com.seravian.core_profile.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "full_name")
    val fullName: String?,
    @ColumnInfo(name = "date_of_birth")
    val dateOfBirth: String?,
    @ColumnInfo(name = "gender")
    val gender: Int?,
    @ColumnInfo(name = "role")
    val role: Int?,
    @ColumnInfo(name = "is_email_verified")
    val isEmailVerified: Boolean,
    @ColumnInfo(name = "is_doctor_verified")
    val isDoctorVerified: Boolean?,
    @ColumnInfo(name = "is_profile_setup_complete")
    val isProfileSetupComplete: Boolean,
)
