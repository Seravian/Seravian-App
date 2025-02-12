package com.greenvenom.navigation.routes

import com.greenvenom.navigation.domain.NavigationTarget
import kotlinx.serialization.Serializable

sealed class SubGraph: NavigationTarget {
    @Serializable
    data object Auth: SubGraph()

    @Serializable
    data object Main: SubGraph()
}