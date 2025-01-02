package com.seravian.seravianapp.navigation.mappers

import androidx.navigation.NavBackStackEntry
import com.seravian.seravianapp.navigation.AppDestination

fun NavBackStackEntry?.fromRoute(): AppDestination? {
    this?.destination?.route?.substringBefore("?")?.substringBefore("/")
        ?.substringAfterLast(".")?.let {
            return when (it) {
                AppDestination.Splash::class.simpleName -> return AppDestination.Splash
                AppDestination.Login::class.simpleName -> return AppDestination.Login
                AppDestination.Register::class.simpleName -> return AppDestination.Register
                AppDestination.VerifyEmail::class.simpleName -> return AppDestination.VerifyEmail
                AppDestination.OTP::class.simpleName -> return AppDestination.OTP
                AppDestination.NewPassword::class.simpleName -> return AppDestination.NewPassword
                AppDestination.Home::class.simpleName -> return AppDestination.Home
                AppDestination.GetUserType::class.simpleName -> AppDestination.GetUserType
                AppDestination.GetUserData::class.simpleName ->AppDestination.GetUserData
                else -> null
            }
        }
    return null
}