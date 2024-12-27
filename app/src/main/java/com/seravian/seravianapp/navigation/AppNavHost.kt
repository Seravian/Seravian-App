package com.seravian.seravianapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.seravian.auth.presentation.login.LoginScreen
import com.seravian.auth.presentation.otp.screen.OtpScreen
import com.seravian.auth.presentation.register.RegisterScreen
import com.seravian.home.presentation.HomeScreen

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val appNavigator = remember(navController) {
        AppNavigator(navController)
    }

    NavHost(
        navController = navController,
        startDestination = AppDestination.Login,
        modifier = modifier
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
            OtpScreen(
                navigate = {  }
            )
        }
        composable<AppDestination.ResetPassword> {

        }
        composable<AppDestination.Home> {
            HomeScreen()
        }
    }
}