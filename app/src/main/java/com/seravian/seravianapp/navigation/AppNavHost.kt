package com.seravian.seravianapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.seravian.auth.presentation.login.LoginScreen
import com.seravian.auth.presentation.register.RegisterScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val appNavigator = remember(navController) {
        AppNavigator(navController)
    }

    NavHost(
        navController = navController,
        startDestination = AppDestination.Login
    ) {
        composable<AppDestination.Login> {
            LoginScreen(
                navigateToRegister = { appNavigator.navigateTo(AppDestination.Register) },
                navigateToEmailVerification = { appNavigator.navigateTo(AppDestination.ResetPassword) },
                navigateToHome = { appNavigator.navigateAndClearBackStack(AppDestination.Home) }
            )
        }
        composable<AppDestination.Register> {
            RegisterScreen(
                navigateBackToLogin = { appNavigator.navigateBack() },
            )
        }
        composable<AppDestination.VerifyEmail> {

        }
        composable<AppDestination.OTP> {

        }
        composable<AppDestination.ResetPassword> {

        }
        composable<AppDestination.Home> {

        }
    }
}