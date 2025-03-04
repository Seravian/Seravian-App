package com.greenvenom.core_navigation.utils

import androidx.navigation.NavDestination
import com.greenvenom.core_navigation.domain.NavigationTarget
import kotlin.reflect.KClass

inline fun <reified NT : NavigationTarget> NavDestination?.toNavigationTarget(navigationType: KClass<out NavigationTarget>): NT? {
    return this?.route?.substringBefore("?")?.substringBefore("/")
        ?.substringAfterLast(".")?.let { identifier ->
            navigationType.sealedSubclasses.firstOrNull {
                it.simpleName == identifier
            }?.objectInstance as? NT
        }
}