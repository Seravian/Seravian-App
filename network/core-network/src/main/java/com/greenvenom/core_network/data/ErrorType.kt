package com.greenvenom.core_network.data

enum class ErrorType {
    // HTTP Status Codes
    BAD_REQUEST,              // 400
    UNAUTHORIZED,             // 401
    FORBIDDEN,                // 403
    NOT_FOUND,                // 404
    REQUEST_TIMEOUT,          // 408
    CONFLICT,                 // 409
    SERIALIZATION_ERROR,      // 422
    TOO_MANY_REQUESTS,        // 429
    SERVER_ERROR,             // 500
    SERVICE_UNAVAILABLE,      // 503

    // Custom Errors
    NO_INTERNET,              // Custom
    UNKNOWN_ERROR             // Custom
}