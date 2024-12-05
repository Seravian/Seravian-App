package com.seravian.seravianapp.features.auth.presentation.register

sealed interface RegisterNavigation {
    data object Home: RegisterNavigation
    data object Idle:RegisterNavigation
    data object NavigateUp:RegisterNavigation
}