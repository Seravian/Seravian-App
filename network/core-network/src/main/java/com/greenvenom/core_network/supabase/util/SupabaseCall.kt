package com.greenvenom.core_network.supabase.util

import com.greenvenom.core_network.data.ErrorType
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.utils.toNetworkError
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.HttpRequestTimeoutException

inline fun <reified T> supabaseCall (
    execute: () -> T
): NetworkResult<T, NetworkError> {
    val result = try {
        val executionResult = execute()
        try {
            NetworkResult.Success(executionResult)
        } catch (e: NoTransformationFoundException) {
            NetworkResult.Error(NetworkError(ErrorType.SERIALIZATION_ERROR))
        }
    } catch (exception: RestException) {
        NetworkResult.Error(toNetworkError(exception.statusCode))
    } catch (exception: HttpRequestException) {
        NetworkResult.Error(NetworkError(ErrorType.NO_INTERNET))
    } catch (exception: HttpRequestTimeoutException) {
        NetworkResult.Error(NetworkError(ErrorType.REQUEST_TIMEOUT))
    }
    return result
}