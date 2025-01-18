package com.seravian.data

import com.seravian.domain.network.Error

data class AuthError(
    val message: String
): Error
