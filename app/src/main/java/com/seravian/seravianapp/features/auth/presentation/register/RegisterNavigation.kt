package com.example.chatapp.screens.registers

sealed interface RegisterNavigation {
    data object Home: RegisterNavigation
    data object Idle:RegisterNavigation
    data object NavigateUp:RegisterNavigation
}