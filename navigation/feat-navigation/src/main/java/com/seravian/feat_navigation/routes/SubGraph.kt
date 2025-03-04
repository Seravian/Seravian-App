package com.seravian.feat_navigation.routes

import com.greenvenom.core_navigation.domain.NavigationTarget
import kotlinx.serialization.Serializable

sealed class SubGraph: NavigationTarget {
    @Serializable
    data object Auth: SubGraph()

    @Serializable
    data object OnBoarding: SubGraph()

    @Serializable
    data object Main: SubGraph()
}