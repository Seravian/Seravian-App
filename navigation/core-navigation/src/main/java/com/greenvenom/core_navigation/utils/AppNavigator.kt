package com.greenvenom.core_navigation.utils

import androidx.navigation.NavHostController
import com.greenvenom.core_navigation.domain.Destination
import kotlin.reflect.KClass

class AppNavigator {
    private lateinit var returnedDestination: KClass<out Destination>
    lateinit var navController: NavHostController
        private set

    fun config(
        returnedDestination: KClass<out Destination>,
        navController: NavHostController
    ) {
        this.returnedDestination = returnedDestination
        this.navController = navController
    }

    fun navigateTo(target: Destination) {
        navController.navigate(target) {
            launchSingleTop = true
        }
    }

    fun navigateBack() {
        navController.navigateUp()
    }

    fun navigateAndClearBackStack(target: Destination) {
        navController.navigate(target) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun navigateFromBottomBar(target: Destination) {
        navController.navigate(target) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun getCurrentDestination(): Destination? {
        return navController.currentBackStackEntry?.destination
            ?.toNavigationTarget<Destination>(returnedDestination)
    }

    fun getPreviousDestination(): Destination? {
        return navController.previousBackStackEntry?.destination
            ?.toNavigationTarget<Destination>(returnedDestination)
    }
}
