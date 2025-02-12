package com.greenvenom.navigation.utils

import androidx.navigation.NavHostController
import com.greenvenom.navigation.domain.NavigationTarget
import com.greenvenom.navigation.routes.Screen

class AppNavigator {
    lateinit var navController: NavHostController
        private set

    fun config(navController: NavHostController) {
        this.navController = navController
    }

    fun navigateTo(target: NavigationTarget): NavigationTarget? {
        navController.navigate(target) {
            launchSingleTop = true
        }
        return getCurrentDestination()
    }

    fun navigateBack(): NavigationTarget? {
        if (hasBackStack()) navController.popBackStack()
        return getCurrentDestination()
    }

    fun navigateAndClearBackStack(target: NavigationTarget): NavigationTarget? {
        navController.navigate(target) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
        return getCurrentDestination()
    }

    fun navigateFromBottomBar(target: NavigationTarget): NavigationTarget? {
        navController.navigate(target) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        return getCurrentDestination()
    }

    fun getCurrentDestination(): NavigationTarget? {
        return navController.currentBackStackEntry?.destination?.toNavigationTarget<NavigationTarget>(Screen::class)
    }

    fun getPreviousDestination(): NavigationTarget? {
        return navController.previousBackStackEntry?.destination?.toNavigationTarget<NavigationTarget>(Screen::class)
    }

    private fun hasBackStack(): Boolean {
        return navController.currentBackStackEntry != null
    }
}
