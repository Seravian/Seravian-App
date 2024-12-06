package com.seravian.seravianapp.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppDestination {
    @Serializable
    data object Splash : AppDestination

    @Serializable
    data object Login : AppDestination

    @Serializable
    data object Register : AppDestination

    @Serializable
    data object Home : AppDestination
}