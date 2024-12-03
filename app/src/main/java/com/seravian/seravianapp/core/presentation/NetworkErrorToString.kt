package com.seravian.seravianapp.core.presentation

import android.content.Context
import com.seravian.seravianapp.R

fun NetworkError.toString(context: Context): String {
    val resId = when (this) {
        NetworkError.REQUEST_TIMEOUT -> R.string.error_request_timeout
        NetworkError.TOO_MANY_REQUESTS -> R.string.error_too_many_requests
        NetworkError.NO_INTERNET -> R.string.error_no_internet
        NetworkError.SERIALIZATION_ERROR -> R.string.error_serialization
        NetworkError.SERVER_ERROR -> R.string.error_server
        NetworkError.UNKNOWN_ERROR -> R.string.error_unknown
    }
    return context.getString(resId)
}