package com.seravian.seravianapp.navigation.components

import androidx.annotation.DrawableRes
import com.greenvenom.navigation.domain.NavigationTarget
import com.seravian.seravianapp.R
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
        icon = R.drawable.sessions_ic,
        label = "Sessions",
        target = Screen.Sessions
    ),
    Doctors(
        icon = R.drawable.person_circle_ic,
        label = "Doctors",
        target = Screen.Doctors
    )
}