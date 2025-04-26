package com.greenvenom.core_network.api.utils

import com.greenvenom.core_network.data.ErrorType
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.utils.toNetworkError
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