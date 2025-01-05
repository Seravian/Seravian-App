package com.seravian.domain.util

import com.seravian.domain.network.Result

fun Result<*, *>.toBoolean() = when(this) {
    is Result.Error -> false
    is Result.Success -> true
}