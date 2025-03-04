package com.greenvenom.core_navigation.utils

import androidx.navigation.NavHostController
import com.greenvenom.core_navigation.domain.NavigationTarget
import kotlin.reflect.KClass

class AppNavigator {
    private lateinit var returnedNavigationType: KClass<out NavigationTarget>
    lateinit var navController: NavHostController
        private set

    fun config(
        returnedNavigationType: KClass<out NavigationTarget>,
        navController: NavHostController
    ) {
        this.returnedNavigationType = returnedNavigationType
        this.navController = navController
    }

    fun navigateTo(target: NavigationTarget): NavigationTarget? {
        navController.navigate(target) {
            launchSingleTop = true
        }
        return getCurrentDestination()
    }

    fun navigateBack(): NavigationTarget? {
        navController.navigateUp()
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
        return navController.currentBackStackEntry?.destination
            ?.toNavigationTarget<NavigationTarget>(returnedNavigationType)
    }

    fun getPreviousDestination(): NavigationTarget? {
        return navController.previousBackStackEntry?.destination
            ?.toNavigationTarget<NavigationTarget>(returnedNavigationType)
    }
}
