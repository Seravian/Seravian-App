package com.greenvenom.core_network.data

import com.greenvenom.core_network.domain.Error

internal typealias DomainError = Error

sealed interface NetworkResult<out D, out E: Error> {
    data class Success<out D>(val data: D): NetworkResult<D, Nothing>
    data class Error<out E: DomainError>(val error: E): NetworkResult<Nothing, E>
}

inline fun <T, E: Error, R> NetworkResult<T, E>.map(map: (T) -> R): NetworkResult<R, E> {
    return when(this) {
        is NetworkResult.Error -> NetworkResult.Error(error)
        is NetworkResult.Success -> NetworkResult.Success(map(data))
    }
}

inline fun <T, E: Error> NetworkResult<T, E>.onSuccess(action: (T) -> Unit): NetworkResult<T, E> {
    return when(this) {
        is NetworkResult.Error -> this
        is NetworkResult.Success -> {
            action(data)
            this
        }
    }
}

inline fun <T, E: Error> NetworkResult<T, E>.onError(action: (E) -> Unit): NetworkResult<T, E> {
    return when(this) {
        is NetworkResult.Error -> {
            action(error)
            this
        }
        is NetworkResult.Success -> this
    }
}