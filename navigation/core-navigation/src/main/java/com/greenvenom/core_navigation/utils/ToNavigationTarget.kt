package com.greenvenom.core_navigation.utils

import androidx.navigation.NavDestination
import com.greenvenom.core_navigation.domain.Destination
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

inline fun <reified NT : Destination> NavDestination?.toNavigationTarget(navigationType: KClass<out Destination>): NT? {
    return this?.route?.substringBefore("?")?.substringBefore("/")
        ?.substringAfterLast(".")?.let { identifier ->
            navigationType.sealedSubclasses.firstOrNull {
                it.simpleName == identifier
            }?.let { kclass ->
                // Try to get the singleton instance first, if available.
                kclass.objectInstance ?: kclass.primaryConstructor?.callBy(emptyMap()) } as? NT
        }
}