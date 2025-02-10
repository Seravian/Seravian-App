package com.greenvenom.navigation.utils

import androidx.navigation.NavHostController
import com.greenvenom.navigation.domain.NavigationTarget
import kotlin.reflect.KClass

class AppNavigator<NT : NavigationTarget> {
    lateinit var navController: NavHostController
    private lateinit var returnDestinationType: KClass<out NavigationTarget>

    fun config(
        navController: NavHostController,
        returnDestinationType: KClass<out NavigationTarget>
    ) {
        this.navController = navController
        if (::returnDestinationType.isInitialized) return
        this.returnDestinationType = returnDestinationType
    }

    fun navigateTo(target: NT): NT? {
        navController.navigate(target) {
            launchSingleTop = true
        }
        return getCurrentDestination()
    }

    fun navigateBack(): NT? {
        if (hasBackStack()) navController.popBackStack()
        return getCurrentDestination()
    }

    fun navigateAndClearBackStack(target: NT): NT? {
        navController.navigate(target) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
        return getCurrentDestination()
    }

    fun navigateFromBottomBar(target: NT): NT? {
        navController.navigate(target) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        return getCurrentDestination()
    }

    fun getCurrentDestination(): NT? {
        val currentDest = navController.currentBackStackEntry?.destination
        return currentDest.toNavigationTarget(returnDestinationType)
    }

    fun getPreviousDestination(): NT? {
        val previousDest = navController.previousBackStackEntry?.destination
        return previousDest.toNavigationTarget(returnDestinationType)
    }

    private fun hasBackStack(): Boolean {
        return navController.currentBackStackEntry != null
    }
}
