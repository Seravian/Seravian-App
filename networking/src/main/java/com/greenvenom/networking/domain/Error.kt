package com.greenvenom.networking.domain

import com.greenvenom.networking.data.ErrorType

abstract class Error {
    open val message: String = ""
    open val errorType: ErrorType? = null
}