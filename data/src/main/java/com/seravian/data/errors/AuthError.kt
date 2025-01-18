package com.seravian.data.errors

import com.greenvenom.networking.data.Error

data class AuthError(
    val message: String
): Error
