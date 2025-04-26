package com.greenvenom.validation.domain

sealed interface ValidationResult<out D, out E: ValidationError> {
    data class Success<out D>(val data: D): ValidationResult<D, Nothing>
    data class Error<out E: ValidationError>(val error: E): ValidationResult<Nothing, E>
}

inline fun <T, E: ValidationError, R> ValidationResult<T, E>.map(map: (T) -> R): ValidationResult<R, E> {
    return when(this) {
        is ValidationResult.Error -> ValidationResult.Error(error)
        is ValidationResult.Success -> ValidationResult.Success(map(data))
    }
}

inline fun <T, E: ValidationError> ValidationResult<T, E>.onSuccess(action: (T) -> Unit): ValidationResult<T, E> {
    return when(this) {
        is ValidationResult.Error -> this
        is ValidationResult.Success -> {
            action(data)
            this
        }
    }
}

inline fun <T, E: ValidationError> ValidationResult<T, E>.onError(action: (E) -> Unit): ValidationResult<T, E> {
    return when(this) {
        is ValidationResult.Error -> {
            action(error)
            this
        }
        is ValidationResult.Success -> this
    }
}