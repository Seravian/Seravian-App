package com.greenvenom.networking.data

enum class ErrorType {
    REQUEST_TIMEOUT,
    TOO_MANY_REQUESTS,
    NO_INTERNET,
    SERIALIZATION_ERROR,
    SERVER_ERROR,
    UNKNOWN_ERROR
}