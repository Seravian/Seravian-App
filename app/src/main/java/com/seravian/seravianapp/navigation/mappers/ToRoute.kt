package com.seravian.seravianapp.navigation.mappers

import com.seravian.seravianapp.navigation.AppDestination

fun AppDestination.toRoute(): String {
    return when (this) {
        is AppDestination.Splash -> "splash"
        is AppDestination.Login -> "login"
        is AppDestination.Register -> "register"
        is AppDestination.VerifyEmail -> "verify_email"
        is AppDestination.OTP -> "otp"
        is AppDestination.ResetPassword -> "reset_password"
        is AppDestination.Home -> "home"
    }
}