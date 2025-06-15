package com.seravian.core_navigation.data.repository

import androidx.compose.runtime.Immutable
import com.seravian.core_navigation.domain.Destination

@Immutable
data class NavigationState(
    val currentDestination: Destination? = null,
    val previousDestination: Destination? = null,
    val isCurrentDestinationSide: Boolean = false,
    val bottomBarState: Boolean = false,
    val topBarState: Boolean = false,
    val accountTypeIndex: Int = 0
)
