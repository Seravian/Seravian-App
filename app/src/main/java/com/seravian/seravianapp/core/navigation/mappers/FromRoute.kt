package com.seravian.seravianapp.core.navigation.mappers

import androidx.navigation.NavBackStackEntry
import com.seravian.seravianapp.core.navigation.AppDestination

fun NavBackStackEntry?.fromRoute(): AppDestination? {
    this?.destination?.route?.substringBefore("?")?.substringBefore("/")
        ?.substringAfterLast(".")?.let {
            return when (it) {
                AppDestination.Home::class.simpleName -> return AppDestination.Home
                AppDestination.Login::class.simpleName -> return AppDestination.Login
                AppDestination.Register::class.simpleName -> return AppDestination.Register
                AppDestination.Splash::class.simpleName -> return AppDestination.Splash
                else -> null
            }
        }
    return null
}