package com.seravian.auth.data

import com.seravian.domain.network.util.Error

data class AuthError(
    val message: String
): Error
