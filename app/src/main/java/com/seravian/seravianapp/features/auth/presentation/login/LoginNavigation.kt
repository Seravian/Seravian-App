package com.seravian.seravianapp.features.auth.presentation.login

sealed interface LoginNavigation {
    data object Home:LoginNavigation
    data object Idle:LoginNavigation
    data object Register:LoginNavigation
}