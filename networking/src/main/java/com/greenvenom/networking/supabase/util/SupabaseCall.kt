package com.greenvenom.networking.supabase.util

import com.greenvenom.networking.data.Result
import com.greenvenom.networking.supabase.data.SupabaseError
import com.greenvenom.networking.supabase.mappers.toError
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.plugins.HttpRequestTimeoutException

suspend inline fun <reified T> supabaseCall (
    execute: () -> T
): Result<T, SupabaseError> {
    val result = try {
        val executionResult = execute()
        Result.Success(executionResult)
    } catch (exception: RestException) {
        Result.Error(exception.toError())
    } catch (exception: HttpRequestException) {
        Result.Error(exception.toError())
    } catch (exception: HttpRequestTimeoutException) {
        Result.Error(exception.toError())
    }

    return result
}