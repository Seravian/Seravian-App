package com.greenvenom.networking.api.data

import com.greenvenom.networking.data.ErrorType
import com.greenvenom.networking.domain.Error

data class APIError(
    override val errorType: ErrorType = ErrorType.UNKNOWN_ERROR,
): Error()
