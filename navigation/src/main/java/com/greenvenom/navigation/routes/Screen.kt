package com.greenvenom.navigation.routes

import com.greenvenom.navigation.domain.NavigationTarget
import kotlinx.serialization.Serializable

sealed class Screen: NavigationTarget {
    @Serializable
    data object Splash: Screen()

    @Serializable
    data object Login: Screen()

    @Serializable
    data object Register: Screen()

    @Serializable
    data object VerifyEmail: Screen()

    @Serializable
    data object OTP: Screen()

    @Serializable
    data object NewPassword: Screen()

    @Serializable
    data object OnBoarding: Screen()

    @Serializable
    data object Home: Screen()

    @Serializable
    data object AIChat: Screen()

    @Serializable
    data object Doctors: Screen()

    @Serializable
    data object Sessions: Screen()

    @Serializable
    data object Profile: Screen()
}