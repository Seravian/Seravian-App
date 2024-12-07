package com.seravian.seravianapp.core.navigation.mappers

import com.seravian.seravianapp.core.navigation.AppDestination

fun AppDestination.toRoute(): String {
    return when (this) {
        is AppDestination.Home -> "home"
        is AppDestination.Login -> "login"
        is AppDestination.Register -> "register"
        is AppDestination.Splash -> "splash"
    }
}