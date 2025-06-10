package com.seravian.feat_navigation.routes

import com.greenvenom.core_navigation.domain.Destination
import com.greenvenom.core_navigation.domain.DestinationType
import kotlinx.serialization.Serializable

sealed class SubGraph: Destination {
    @Serializable
    data object Auth: SubGraph() {
        override val destinationType: DestinationType = DestinationType.GRAPH
    }

    @Serializable
    data object OnBoarding: SubGraph() {
        override val destinationType: DestinationType = DestinationType.GRAPH
    }

    @Serializable
    data object Patient: SubGraph() {
        override val destinationType: DestinationType = DestinationType.GRAPH
    }

    @Serializable
    data object AIChat: SubGraph() {
        override val destinationType: DestinationType = DestinationType.GRAPH
    }

    @Serializable
    data object Doctor: SubGraph() {
        override val destinationType: DestinationType = DestinationType.GRAPH
    }
}