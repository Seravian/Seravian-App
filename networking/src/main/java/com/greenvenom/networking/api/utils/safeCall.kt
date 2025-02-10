package com.greenvenom.networking.api.utils

import com.greenvenom.networking.api.data.APIError
import com.greenvenom.networking.data.Result
import com.greenvenom.networking.data.ErrorType
import com.greenvenom.networking.domain.Error
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall (
    execute: () -> HttpResponse
): Result<T, Error> {
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        return Result.Error(APIError(ErrorType.NO_INTERNET))
    } catch (e: SerializationException) {
        return Result.Error(APIError(ErrorType.SERIALIZATION_ERROR))
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return Result.Error(APIError(ErrorType.UNKNOWN_ERROR))
    }
    return responseToResult(response)
}