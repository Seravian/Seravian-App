package com.seravian.seravianapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.seravian.auth.presentation.login.LoginScreen
import com.seravian.auth.presentation.otp.OtpScreen
import com.seravian.auth.presentation.register.RegisterScreen
import com.seravian.auth.presentation.reset_password.screen.NewPasswordScreen
import com.seravian.auth.presentation.reset_password.screen.VerifyEmailScreen
import com.seravian.home.presentation.home.HomeScreen
import com.seravian.home.presentation.survey.GetUserData
import com.seravian.home.presentation.survey.GetUserType

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
                navigateToRegisterScreen = { appNavigator.navigateTo(AppDestination.Register) },
                navigateToEmailVerificationScreen = { appNavigator.navigateTo(AppDestination.VerifyEmail) },
                navigateToHomeScreen = { appNavigator.navigateAndClearBackStack(AppDestination.Home) },
                navigateToSurvey = {appNavigator.navigateAndClearBackStack(AppDestination.GetUserType)}
            )
        }
        composable<AppDestination.Register> {
            RegisterScreen(
                navigateBack = { appNavigator.navigateBack() },
                navigateToLoginScreen = { appNavigator.navigateAndClearBackStack(AppDestination.Login) }
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
                navigateToNewPasswordScreen = { appNavigator.navigateAndClearBackStack(AppDestination.NewPassword) },
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

        composable<AppDestination.GetUserType> {
            GetUserType(
                navigateToGetUserData = {appNavigator.navigateTo(AppDestination.GetUserData)}
            )
        }

        composable<AppDestination.GetUserData> {
            GetUserData(
                navigateToHome = {appNavigator.navigateTo(AppDestination.Home)}
            )
        }
    }
}

