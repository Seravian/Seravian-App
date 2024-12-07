package com.seravian.seravianapp.core.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.seravian.seravianapp.core.navigation.mappers.fromRoute
import com.seravian.seravianapp.core.navigation.mappers.toRoute

class AppNavigator(private val navController: NavHostController) {
    fun navigateTo(destination: AppDestination) {
        navController.navigate(destination)
    }

    fun navigateBack(): AppDestination? {
        if (hasBackStack()) navController.popBackStack()
        return getCurrentDestination()
    }

    fun navigateAndClearBackStack(destination: AppDestination) {
        navController.navigate(destination) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
        }
    }

    fun getCurrentDestination(): AppDestination? {
        if (hasBackStack()) {
            val backstackEntry = navController.currentBackStackEntry
            val currentDestination = backstackEntry.fromRoute()

            return currentDestination
        } else return AppDestination.Splash
    }

    fun hasBackStack(): Boolean {
        return navController.previousBackStackEntry != null
    }
}
