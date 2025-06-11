package com.seravian.core_profile.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.seravian.core_profile.domain.Profile
import com.seravian.core_profile.domain.utils.Gender
import com.seravian.core_profile.domain.utils.Role
import com.seravian.core_verification.domain.utils.DoctorTitle

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
    @ColumnInfo(name = "created_at_utc")
    val createdAtUtc: String,
    @ColumnInfo(name = "gender")
    val gender: Gender?,
    @ColumnInfo(name = "role")
    val role: Role?,
    @ColumnInfo(name = "is_email_verified")
    val isEmailVerified: Boolean,
    @ColumnInfo(name = "is_profile_setup_complete")
    val isProfileSetupComplete: Boolean,
    @ColumnInfo(name = "doctor_title")
    val doctorTitle: DoctorTitle?,
    @ColumnInfo(name = "doctor_description")
    val doctorDescription: String?,
    @ColumnInfo(name = "doctor_session_price")
    val doctorSessionPrice: Int?,
    @ColumnInfo(name = "doctor_verified_at_utc")
    val doctorVerifiedAtUtc: String?,
)

fun ProfileEntity.extractProfile(): Profile {
    return Profile(
        id = id,
        email = email,
        fullName = fullName,
        dateOfBirth = dateOfBirth,
        createdAtUtc = createdAtUtc,
        gender = gender,
        role = role,
        isEmailVerified = isEmailVerified,
        isProfileSetupComplete = isProfileSetupComplete,
        doctorTitle = doctorTitle,
        doctorDescription = doctorDescription,
        doctorSessionPrice = doctorSessionPrice,
        doctorVerifiedAtUtc = doctorVerifiedAtUtc,
    )
}

