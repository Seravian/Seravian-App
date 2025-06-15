package com.seravian.core_navigation.data

import com.seravian.core_navigation.domain.Destination

sealed class NavigationType {
    data object Back : NavigationType()
    data class ClearBackStack(val destination: Destination) : NavigationType()
    data class BottomNavigation(val destination: Destination) : NavigationType()
    data class Standard(val destination: Destination) : NavigationType()
}
