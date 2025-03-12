package com.greenvenom.core_navigation.data

import com.greenvenom.core_navigation.domain.Destination

sealed class NavigationType {
    data object Back : NavigationType()
    data class ClearBackStack(val destination: Destination) : NavigationType()
    data class BottomNavigation(val destination: Destination) : NavigationType()
    data class Standard(val destination: Destination) : NavigationType()
}
