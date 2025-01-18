package com.seravian.ui.util.network

import android.content.Context
import com.greenvenom.networking.data.NetworkError
import com.seravian.ui.R

fun com.greenvenom.networking.data.NetworkError.toString(context: Context): String {
    val resId = when (this) {
        com.greenvenom.networking.data.NetworkError.REQUEST_TIMEOUT -> R.string.error_request_timeout
        com.greenvenom.networking.data.NetworkError.TOO_MANY_REQUESTS -> R.string.error_too_many_requests
        com.greenvenom.networking.data.NetworkError.NO_INTERNET -> R.string.error_no_internet
        com.greenvenom.networking.data.NetworkError.SERIALIZATION_ERROR -> R.string.error_serialization
        com.greenvenom.networking.data.NetworkError.SERVER_ERROR -> R.string.error_server
        com.greenvenom.networking.data.NetworkError.UNKNOWN_ERROR -> R.string.error_unknown
    }
    return context.getString(resId)
}