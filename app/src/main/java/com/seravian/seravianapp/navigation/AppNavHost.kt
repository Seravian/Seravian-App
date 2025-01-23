package com.seravian.seravianapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.greenvenom.navigation.AppDestination
import com.greenvenom.navigation.AppDestinationSaver
import com.greenvenom.navigation.AppNavigator
import com.seravian.auth.presentation.login.LoginScreen
import com.seravian.auth.presentation.otp.OtpScreen
import com.seravian.auth.presentation.register.RegisterScreen
import com.seravian.auth.presentation.reset_password.screens.NewPasswordScreen
import com.seravian.auth.presentation.reset_password.screens.VerifyEmailScreen
import com.seravian.auth.presentation.splash.SplashScreen
import com.seravian.onboarding.presentation.screens.OnBoardingScreen
import com.seravian.home.presentation.HomeScreen
import com.greenvenom.navigation.presentation.BottomNavigationBar
import com.seravian.ui.components.TopAppBar
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val appNavigator = remember(navController) {
        AppNavigator(navController)
    }
    val currentDestination = rememberSaveable(stateSaver = AppDestinationSaver) {
        mutableStateOf(appNavigator.getCurrentDestination())
    }
    val timer: Timer = fixedRateTimer(initialDelay = 0, period = 1000) {
        println("Triggered at: $currentDestination")
    }

    val bottomBarState = rememberSaveable { mutableStateOf(true) }
    val topBarState = rememberSaveable { mutableStateOf(true) }

    when (currentDestination.value) {
        AppDestination.Home,
        AppDestination.AIChat,
        AppDestination.Sessions,
        AppDestination.Doctors -> {
            bottomBarState.value = true
            topBarState.value = true
        }
        else -> {
            bottomBarState.value = false
            topBarState.value = false
        }
    }

    Scaffold(
        topBar = { TopAppBar(topBarState.value) },
        bottomBar = { BottomNavigationBar(
            navigateToHome = {
                currentDestination.value = appNavigator.navigateAndClearBackStack(AppDestination.Home)
            },
            navigateToAIChat = {
                currentDestination.value = appNavigator.navigateTo(AppDestination.AIChat)
            },
            navigateToSessions = {
                currentDestination.value = appNavigator.navigateTo(AppDestination.Sessions)
            },
            navigateToDoctors = {
                currentDestination.value = appNavigator.navigateTo(AppDestination.Doctors)
            },
            currentDestination = currentDestination.value,
            isVisible = bottomBarState.value
        ) },
        modifier = modifier,
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = AppDestination.Splash,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<AppDestination.Splash> {
                    SplashScreen { currentDestination.value = appNavigator.navigateTo(AppDestination.Login) }
                }

                composable<AppDestination.Login> {
                    LoginScreen(
                        navigateToRegisterScreen = {
                            currentDestination.value = appNavigator.navigateTo(AppDestination.Register)
                        },
                        navigateToEmailVerificationScreen = {
                            currentDestination.value = appNavigator.navigateTo(AppDestination.VerifyEmail)
                        },
                        navigateToHomeScreen = {
                            currentDestination.value = appNavigator.navigateAndClearBackStack(AppDestination.Home)
                        },
                        navigateToForm = {
                            currentDestination.value = appNavigator.navigateAndClearBackStack(AppDestination.OnBoarding)
                        }
                    )
                }

                composable<AppDestination.Register> {
                    RegisterScreen(
                        navigateBack = { currentDestination.value = appNavigator.navigateBack() },
                        navigateToLoginScreen = {
                            currentDestination.value = appNavigator.navigateAndClearBackStack(AppDestination.Login)
                        }
                    )
                }

                composable<AppDestination.VerifyEmail> {
                    VerifyEmailScreen(
                        navigateBack = { currentDestination.value = appNavigator.navigateBack() },
                        navigateToOtpScreen = {
                            currentDestination.value = appNavigator.navigateTo(AppDestination.OTP)
                        }
                    )
                }

                composable<AppDestination.OTP> {
                    OtpScreen(
                        navigateToNewPasswordScreen = {
                            currentDestination.value = appNavigator.navigateAndClearBackStack(AppDestination.NewPassword)
                        },
                        navigateBack = {  currentDestination.value = appNavigator.navigateBack() }
                    )
                }

                composable<AppDestination.NewPassword> {
                    NewPasswordScreen(
                        navigateBack = { currentDestination.value = appNavigator.navigateBack() },
                        navigateToLoginScreen = {
                            currentDestination.value = appNavigator.navigateTo(AppDestination.Login)
                        }
                    )
                }

                composable<AppDestination.OnBoarding> {
                    OnBoardingScreen(
                        navigateToHomeScreen = {
                            currentDestination.value = appNavigator.navigateAndClearBackStack(AppDestination.Home)
                        }
                    )
                }

                composable<AppDestination.Home> {
                    HomeScreen(

                    )
                }

                composable<AppDestination.AIChat> {

                }

                composable<AppDestination.Sessions> {

                }

                composable<AppDestination.Doctors> {

                }
            }
        }
    )
}

