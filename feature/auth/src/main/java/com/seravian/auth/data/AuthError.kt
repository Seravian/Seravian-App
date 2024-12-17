package com.seravian.auth.data

import com.seravian.domain.network.Error

data class AuthError(
    val message: String
): Error
