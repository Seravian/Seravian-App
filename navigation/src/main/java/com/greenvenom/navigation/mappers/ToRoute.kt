package com.greenvenom.navigation.mappers

import com.greenvenom.navigation.AppDestination

fun AppDestination.toRoute(): String {
    return when (this) {
        is AppDestination.Splash -> "splash"
        is AppDestination.Login -> "login"
        is AppDestination.Register -> "register"
        is AppDestination.VerifyEmail -> "verify_email"
        is AppDestination.OTP -> "otp"
        is AppDestination.NewPassword -> "new_password"
        is AppDestination.Home -> "home"
        is AppDestination.OnBoarding -> "onboarding"
        is AppDestination.AIChat -> "ai_chat"
        is AppDestination.Doctors -> "doctors"
        is AppDestination.Sessions -> "sessions"
        is AppDestination.Profile -> "profile"
    }
}