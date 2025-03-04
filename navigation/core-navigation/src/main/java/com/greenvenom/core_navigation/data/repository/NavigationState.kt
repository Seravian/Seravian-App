package com.greenvenom.core_navigation.data.repository

import androidx.compose.runtime.Immutable
import com.greenvenom.core_navigation.domain.NavigationTarget

@Immutable
data class NavigationState(
    val currentDestination: NavigationTarget? = null,
    val previousDestination: NavigationTarget? = null,
    val bottomBarState: Boolean = false,
    val topBarState: Boolean = false
)
