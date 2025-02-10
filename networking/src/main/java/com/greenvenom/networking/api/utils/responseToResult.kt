package com.greenvenom.networking.api.utils

import com.greenvenom.networking.api.data.APIError
import com.greenvenom.networking.data.Result
import com.greenvenom.networking.data.ErrorType
import com.greenvenom.networking.domain.Error
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, Error> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch (e: NoTransformationFoundException) {
                Result.Error(APIError(ErrorType.SERIALIZATION_ERROR))
            }
        }
        408 -> Result.Error(APIError(ErrorType.REQUEST_TIMEOUT))
        429 -> Result.Error(APIError(ErrorType.TOO_MANY_REQUESTS))
        in 500..599 -> Result.Error(APIError(ErrorType.SERVER_ERROR))
        else -> Result.Error(APIError(ErrorType.UNKNOWN_ERROR))
    }
}