package com.greenvenom.networking.api.utils

import com.greenvenom.networking.data.NetworkResult
import com.greenvenom.networking.data.ErrorType
import com.greenvenom.networking.data.NetworkError
import com.greenvenom.networking.utils.toNetworkError
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): NetworkResult<T, NetworkError> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                NetworkResult.Success(response.body<T>())
            } catch (e: NoTransformationFoundException) {
                NetworkResult.Error(NetworkError(ErrorType.SERIALIZATION_ERROR))
            }
        }
        else -> NetworkResult.Error(toNetworkError(response.status.value))
    }
}