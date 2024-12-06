package com.seravian.seravianapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.seravian.seravianapp.features.auth.authNavGraph
import com.seravian.seravianapp.splash.presentation.SplashScreen

@Composable
private fun AppNavGraph() {
    val navController = rememberNavController()
    val appNavigator = remember(navController) {
        AppNavigator(navController)
    }

    NavHost(
        navController = navController,
        startDestination = AppDestination.Splash
    ) {
        composable<AppDestination.Splash> {
            SplashScreen(appNavigator)
        }
        authNavGraph(appNavigator)
    }
}