package com.greenvenom.networking.supabase.mappers

import com.greenvenom.networking.supabase.data.SupabaseError
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.plugins.HttpRequestTimeoutException

fun RestException.toError(): SupabaseError {
    return SupabaseError(
        message = error,
        description = description,
    )
}

fun HttpRequestException.toError(): SupabaseError {
    val error = localizedMessage?.let {
        SupabaseError(
            message = it,
        )
    }
    return error ?: SupabaseError(
        message = "The request failed.",
    )
}

fun HttpRequestTimeoutException.toError(): SupabaseError {
    return SupabaseError(
        message = "The request time out.",
    )
}