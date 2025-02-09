package com.greenvenom.navigation.components

import androidx.annotation.DrawableRes
import com.greenvenom.navigation.domain.NavigationTarget
import com.greenvenom.navigation.R
import com.greenvenom.navigation.routes.Screen

enum class BottomDestination(
    @DrawableRes val icon: Int,
    val label: String,
    val target: NavigationTarget,
) {
    Home(
        icon = R.drawable.home_ic,
        label = "Home",
        target = Screen.Home
    ),
    AIChat(
        icon = R.drawable.chat_ic,
        label = "AI Chat",
        target = Screen.AIChat
    ),
    Sessions(
        icon = R.drawable.activity_ic,
        label = "Sessions",
        target = Screen.Activity
    ),
    Doctors(
        icon = R.drawable.person_circle_ic,
        label = "Doctors",
        target = Screen.Doctors
    )
}