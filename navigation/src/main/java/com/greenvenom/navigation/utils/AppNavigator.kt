package com.greenvenom.navigation.utils

import androidx.navigation.NavHostController
import com.greenvenom.navigation.domain.NavigationTarget
import kotlin.reflect.KClass

class AppNavigator<NT : NavigationTarget>(private val returnDestinationType: KClass<out NavigationTarget>) {
    lateinit var navController: NavHostController

    fun config(navController: NavHostController) {
        this.navController = navController
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
