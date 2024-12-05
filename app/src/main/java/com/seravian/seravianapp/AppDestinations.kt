package com.seravian.seravianapp

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppDestinations {
    @Serializable
    data object Splash : AppDestinations

    @Serializable
    data object Login : AppDestinations

    @Serializable
    data object Register : AppDestinations

    @Serializable
    data object Home : AppDestinations
}