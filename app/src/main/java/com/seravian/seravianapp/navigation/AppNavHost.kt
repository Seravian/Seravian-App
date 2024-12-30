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
import com.seravian.auth.presentation.reset_password.screen.NewPasswordScreen
import com.seravian.auth.presentation.reset_password.screen.VerifyEmailScreen
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
    ) {
        composable<AppDestination.Login> {
            LoginScreen(
                navigateToRegister = { appNavigator.navigateTo(AppDestination.Register) },
                navigateToEmailVerification = { appNavigator.navigateTo(AppDestination.VerifyEmail) },
                navigateToHome = { appNavigator.navigateAndClearBackStack(AppDestination.Home) }
            )
        }
        composable<AppDestination.Register> {
            RegisterScreen(
                navigateBack = { appNavigator.navigateBack() },
            )
        }
        composable<AppDestination.VerifyEmail> {
            VerifyEmailScreen(
                navigateBack = { appNavigator.navigateBack() },
                navigateToOtpScreen = { appNavigator.navigateTo(AppDestination.OTP) }
            )
        }
        composable<AppDestination.OTP> {
            OtpScreen(
                navigateTo = { appNavigator.navigateAndClearBackStack(AppDestination.NewPassword) },
                navigateBack = { appNavigator.navigateBack() }
            )
        }
        composable<AppDestination.NewPassword> {
            NewPasswordScreen(
                navigateBack = { appNavigator.navigateBack() },
                navigateToLoginScreen = { appNavigator.navigateTo(AppDestination.Login) }
            )
        }
        composable<AppDestination.Home> {
            HomeScreen()
        }
    }
}