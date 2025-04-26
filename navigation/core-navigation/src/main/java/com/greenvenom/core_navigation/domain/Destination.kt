package com.greenvenom.core_navigation.domain

enum class DestinationType {
    GRAPH,
    MAIN,
    SIDE,
    AUTH,
    OTHER
}

interface Destination {
    val destinationType: DestinationType
}