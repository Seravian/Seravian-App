package com.greenvenom.core_network.utils

import android.content.Context
import com.greenvenom.core_network.R
import com.greenvenom.core_network.data.ErrorType

fun ErrorType.toString(context: Context): String {
    val resId = when (this) {
        ErrorType.BAD_REQUEST -> R.string.error_bad_request
        ErrorType.UNAUTHORIZED -> R.string.error_unauthorized
        ErrorType.FORBIDDEN -> R.string.error_forbidden
        ErrorType.NOT_FOUND -> R.string.error_not_found
        ErrorType.REQUEST_TIMEOUT -> R.string.error_request_timeout
        ErrorType.CONFLICT -> R.string.error_conflict
        ErrorType.SERIALIZATION_ERROR -> R.string.error_serialization
        ErrorType.TOO_MANY_REQUESTS -> R.string.error_too_many_requests
        ErrorType.SERVER_ERROR -> R.string.error_server
        ErrorType.SERVICE_UNAVAILABLE -> R.string.error_service_unavailable
        ErrorType.NO_INTERNET -> R.string.error_no_internet
        ErrorType.UNKNOWN_ERROR -> R.string.error_unknown
    }
    return context.getString(resId)
}