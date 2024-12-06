package com.seravian.seravianapp.core.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

class AppNavigator(private val navController: NavHostController) {
    fun navigateTo(destination: AppDestination) {
        navController.navigate(destination)
    }

    fun navigateBack() {
        navController.popBackStack()
    }

    fun navigateAndClearBackStack(destination: AppDestination) {
        navController.navigate(destination) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
        }
    }

    fun hasBackStack(): Boolean {
        return navController.previousBackStackEntry != null
    }
}