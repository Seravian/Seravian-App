package com.seravian.feat_navigation.routes

import com.greenvenom.core_navigation.domain.Destination
import com.greenvenom.core_navigation.domain.DestinationType
import kotlinx.serialization.Serializable

sealed class Screen: Destination {
    @Serializable
    data object Splash: Screen() {
        override val destinationType: DestinationType = DestinationType.AUTH
    }

    @Serializable
    data object Login: Screen() {
        override val destinationType: DestinationType = DestinationType.AUTH
    }

    @Serializable
    data object Register: Screen() {
        override val destinationType: DestinationType = DestinationType.AUTH
    }

    @Serializable
    data object VerifyEmail: Screen() {
        override val destinationType: DestinationType = DestinationType.AUTH
    }

    @Serializable
    data object OTP: Screen() {
        override val destinationType: DestinationType = DestinationType.AUTH
    }

    @Serializable
    data object NewPassword: Screen() {
        override val destinationType: DestinationType = DestinationType.AUTH
    }

    @Serializable
    data object OnBoarding: Screen() {
        override val destinationType: DestinationType = DestinationType.OTHER
    }

    @Serializable
    data object Home: Screen() {
        override val destinationType: DestinationType = DestinationType.MAIN
    }

    @Serializable
    data object ChatsList: Screen() {
        override val destinationType: DestinationType = DestinationType.MAIN
    }

    @Serializable
    data class Chat(val chatId: String = ""): Screen() {
        override val destinationType: DestinationType = DestinationType.SIDE
    }

    @Serializable
    data object VoiceMode: Screen() {
        override val destinationType: DestinationType = DestinationType.SIDE
    }

    @Serializable
    data object Doctors: Screen() {
        override val destinationType: DestinationType = DestinationType.MAIN
    }
    @Serializable
    data class DoctorDetails(val doctorId: String = ""): Screen() {
        override val destinationType: DestinationType = DestinationType.SIDE
    }

    @Serializable
    data object Sessions: Screen() {
        override val destinationType: DestinationType = DestinationType.MAIN
    }

    @Serializable
    data object Profile: Screen() {
        override val destinationType: DestinationType = DestinationType.MAIN
    }


}