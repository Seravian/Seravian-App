package com.seravian.feat_navigation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.greenvenom.core_navigation.domain.Destination
import com.seravian.feat_navigation.R
import com.seravian.feat_navigation.routes.Screen
import com.seravian.feat_navigation.routes.SubGraph

enum class PatientDestination(
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
        target = SubGraph.AIChat
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
    ),
    Profile(
        icon = R.drawable.person_ic,
        label = R.string.profile,
        target = Screen.PatientProfile
    )
}