package com.seravian.feat_navigation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.greenvenom.core_navigation.domain.Destination
import com.seravian.feat_navigation.R
import com.seravian.feat_navigation.routes.Screen

enum class BottomDestination(
    @DrawableRes val icon: Int,
    @StringRes val label: Int,
    val target: Destination,
) {
    Home(
        icon = R.drawable.home_ic,
        label = R.string.home,
        target = Screen.Home
    ),
    AIChat(
        icon = R.drawable.chat_ic,
        label = R.string.ai_chat,
        target = Screen.ChatsList
    ),
    Sessions(
        icon = R.drawable.sessions_ic,
        label = R.string.sessions,
        target = Screen.Sessions
    ),
    Doctors(
        icon = R.drawable.person_circle_ic,
        label = R.string.doctors,
        target = Screen.Doctors
    )
}