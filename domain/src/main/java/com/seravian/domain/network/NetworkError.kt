package com.seravian.domain.network

enum class NetworkError: Error {
    REQUEST_TIMEOUT,
    TOO_MANY_REQUESTS,
    NO_INTERNET,
    SERIALIZATION_ERROR,
    SERVER_ERROR,
    UNKNOWN_ERROR
}