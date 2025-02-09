package com.greenvenom.networking.utils

import android.content.Context
import com.greenvenom.networking.R
import com.greenvenom.networking.data.ErrorType

fun ErrorType.toString(context: Context): String {
    val resId = when (this) {
        ErrorType.REQUEST_TIMEOUT -> R.string.error_request_timeout
        ErrorType.TOO_MANY_REQUESTS -> R.string.error_too_many_requests
        ErrorType.NO_INTERNET -> R.string.error_no_internet
        ErrorType.SERIALIZATION_ERROR -> R.string.error_serialization
        ErrorType.SERVER_ERROR -> R.string.error_server
        ErrorType.UNKNOWN_ERROR -> R.string.error_unknown
    }
    return context.getString(resId)
}