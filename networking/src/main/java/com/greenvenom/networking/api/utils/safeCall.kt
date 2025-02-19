package com.greenvenom.networking.api.utils

import com.greenvenom.networking.data.NetworkResult
import com.greenvenom.networking.data.ErrorType
import com.greenvenom.networking.data.NetworkError
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall (
    execute: () -> HttpResponse
): NetworkResult<T, NetworkError> {
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        return NetworkResult.Error(NetworkError(ErrorType.NO_INTERNET))
    } catch (e: SerializationException) {
        return NetworkResult.Error(NetworkError(ErrorType.SERIALIZATION_ERROR))
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return NetworkResult.Error(NetworkError(ErrorType.UNKNOWN_ERROR))
    }
    return responseToResult(response)
}