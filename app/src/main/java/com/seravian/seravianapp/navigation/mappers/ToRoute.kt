package com.seravian.seravianapp.navigation.mappers

import com.seravian.seravianapp.navigation.AppDestination

fun AppDestination.toRoute(): String {
    return when (this) {
        is AppDestination.Splash -> "splash"
        is AppDestination.Login -> "login"
        is AppDestination.Register -> "register"
        is AppDestination.VerifyEmail -> "verify_email"
        is AppDestination.OTP -> "otp"
        is AppDestination.NewPassword -> "new_password"
        is AppDestination.Home -> "home"
        is AppDestination.GetUserType -> "get_user_type"
        is AppDestination.GetUserData -> "get_user_data"
    }
}