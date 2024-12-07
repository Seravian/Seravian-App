package com.seravian.seravianapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.seravian.seravianapp.features.auth.presentation.login.LoginScreen
import com.seravian.seravianapp.features.auth.presentation.register.RegisterScreen
import com.seravian.seravianapp.splash.presentation.SplashScreen

@Composable
fun AppNavGraph() {
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
        composable<AppDestination.Login> {
            LoginScreen(appNavigator)
        }
        composable<AppDestination.Register> {
            RegisterScreen(appNavigator)
        }
        composable<AppDestination.Home> {

        }
    }
}