package com.greenvenom.navigation

import androidx.navigation.NavHostController
import com.greenvenom.navigation.mappers.fromRoute

class AppNavigator(private val navController: NavHostController) {
    fun navigateTo(destination: AppDestination): AppDestination {
        navController.navigate(destination) {
            launchSingleTop = true
        }
        return getCurrentDestination()
    }

    fun navigateBack(): AppDestination {
        if (hasBackStack()) navController.popBackStack()
        return getCurrentDestination()
    }

    fun navigateAndClearBackStack(destination: AppDestination): AppDestination {
        navController.navigate(destination) {
            popUpTo(0) {
                inclusive = true
            }
            launchSingleTop = true
        }
        return getCurrentDestination()
    }

    fun getCurrentDestination(): AppDestination {
        val backstackEntry = navController.currentBackStackEntry
        val currentDestination = backstackEntry.fromRoute()

        return currentDestination
    }

    private fun hasBackStack(): Boolean {
        return navController.previousBackStackEntry != null
    }
}
