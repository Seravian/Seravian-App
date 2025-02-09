package com.greenvenom.networking.domain

import com.greenvenom.networking.data.ErrorType

abstract class NetworkError {
    open val message: String = ""
    open val errorType: ErrorType? = null
}