package com.seravian.feat_navigation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.seravian.core_navigation.domain.Destination
import com.seravian.feat_navigation.R
import com.seravian.feat_navigation.routes.Screen

enum class PatientDestination(
    @DrawableRes val icon: Int,
    @StringRes val label: Int,
    val target: Destination,
) {
    AIChat(
        icon = R.drawable.chatbot_ic,
        label = R.string.ai_chat,
        target = Screen.ChatsList
    ),
    Home(
        icon = R.drawable.ic_support,
        label = R.string.support,
        target = Screen.Home
    ),
//    Sessions(
//        icon = R.drawable.sessions_ic,
//        label = R.string.sessions,
//        target = Screen.Sessions
//    ),
//    Doctors(
//        icon = R.drawable.person_circle_ic,
//        label = R.string.doctors,
//        target = Screen.Doctors
//    ),
    Profile(
        icon = R.drawable.person_ic,
        label = R.string.profile,
        target = Screen.PatientProfile
    )
}