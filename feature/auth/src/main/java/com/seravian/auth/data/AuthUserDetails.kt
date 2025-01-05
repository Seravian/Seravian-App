package com.seravian.auth.data

data class AuthUserDetails(
    val fullName: String? = null,
    val userType: String? = null,
    val phoneNumber: String? = null,
    val birthDate: String? = null,
    val gender: String? = null,
)
