package com.greenvenom.core_network.utils

import com.greenvenom.core_network.data.ErrorType
import com.greenvenom.core_network.data.NetworkError

fun toNetworkError(statusErrorCode: Int): NetworkError {
    return NetworkError(
        errorType = when (statusErrorCode) {
            400 -> ErrorType.BAD_REQUEST
            401 -> ErrorType.UNAUTHORIZED
            403 -> ErrorType.FORBIDDEN
            404 -> ErrorType.NOT_FOUND
            408 -> ErrorType.REQUEST_TIMEOUT
            409 -> ErrorType.CONFLICT
            422 -> ErrorType.SERIALIZATION_ERROR
            429 -> ErrorType.TOO_MANY_REQUESTS
            500 -> ErrorType.SERVER_ERROR
            503 -> ErrorType.SERVICE_UNAVAILABLE
            else -> ErrorType.UNKNOWN_ERROR
        },
    )
}