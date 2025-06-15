package com.seravian.feat_navigation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.seravian.core_navigation.domain.Destination
import com.seravian.feat_navigation.R
import com.seravian.feat_navigation.routes.Screen

enum class DoctorDestination(
    @DrawableRes val icon: Int,
    @StringRes val label: Int,
    val target: Destination,
) {
    Appointments(
        icon = R.drawable.sessions_ic,
        label = R.string.appointments,
        target = Screen.DoctorAppointments
    ),
    Requests(
        icon = R.drawable.requests_ic,
        label = R.string.requests,
        target = Screen.DoctorRequests
    ),
    Profile(
        icon = R.drawable.person_ic,
        label = R.string.profile,
        target = Screen.DoctorProfile
    ),
}