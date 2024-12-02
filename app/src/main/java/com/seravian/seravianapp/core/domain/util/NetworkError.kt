package com.seravian.seravianapp.core.domain.util

enum class NetworkError: Error {
    REQUEST_TIMEOUT,
    TOO_MANY_REQUESTS,
    NO_INTERNET,
    SERIALIZATION_ERROR,
    SERVER_ERROR,
    UNKNOWN_ERROR
}