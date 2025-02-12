package com.seravian.seravianapp.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.greenvenom.auth.presentation.login.LoginScreen
import com.greenvenom.auth.presentation.otp.OtpScreen
import com.greenvenom.auth.presentation.register.RegisterScreen
import com.greenvenom.auth.presentation.reset_password.screens.NewPasswordScreen
import com.greenvenom.auth.presentation.reset_password.screens.VerifyEmailScreen
import com.greenvenom.auth.presentation.splash.SplashScreen
import com.greenvenom.navigation.domain.NavigationTarget
import com.greenvenom.navigation.data.NavigationType
import com.greenvenom.navigation.routes.Screen
import com.greenvenom.navigation.routes.SubGraph
import com.greenvenom.navigation.repository.NavigationStateRepository
import com.greenvenom.navigation.utils.AppNavigator
import com.seravian.home.presentation.HomeScreen
import com.seravian.onboarding.presentation.screens.OnBoardingScreen
import org.koin.compose.koinInject

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val appNavigator = koinInject<AppNavigator>()
    val navigationStateRepository = koinInject<NavigationStateRepository>()
    val navigationState by navigationStateRepository.navigationState.collectAsStateWithLifecycle()

    appNavigator.config(navController = rememberNavController())
    navigationStateRepository.config(
        enableBarsDestinations = setOf(
            Screen.Home,
            Screen.AIChat,
            Screen.Sessions,
            Screen.Doctors
        )
    )

    NavHost(
        navController = appNavigator.navController,
        startDestination = SubGraph.Auth,
        modifier = modifier
    ) {
        composable<Screen.Splash> {
            SplashScreen()
        }

        navigation<SubGraph.Auth>(startDestination = Screen.Login) {
            composable<Screen.Login> {
                LoginScreen(
                    navigateToRegisterScreen = {
                        navigationStateRepository.updateDestination(NavigationType.Standard(Screen.Register))
                    },
                    navigateToEmailVerificationScreen = {
                        navigationStateRepository.updateDestination(NavigationType.Standard(Screen.VerifyEmail))
                    },
                    navigateToNextScreen = {
                        navigationStateRepository.updateDestination(NavigationType.ClearBackStack(
                            SubGraph.Main))
                    },
                )
            }
            composable<Screen.Register> {
                RegisterScreen(
                    navigateBack = {
                        navigationStateRepository.updateDestination(NavigationType.Back)
                    },
                    navigateToAccountVerificationScreen = {
                        navigationStateRepository.updateDestination(NavigationType.Standard(Screen.OTP))
                    }
                )
            }
            composable<Screen.VerifyEmail> {
                VerifyEmailScreen(
                    navigateBack = {
                        navigationStateRepository.updateDestination(NavigationType.Back)
                    },
                    navigateToOtpScreen = {
                        navigationStateRepository.updateDestination(NavigationType.Standard(Screen.OTP))
                    }
                )
            }
            composable<Screen.OTP> {
                OtpScreen(
                    navigateBack = {
                        navigationStateRepository.updateDestination(NavigationType.Back)
                    },
                    navigateToNewPasswordScreen = {
                        if (navigationState.previousDestination is Screen.VerifyEmail) {
                            navigationStateRepository.updateDestination(NavigationType.ClearBackStack(
                                Screen.NewPassword))
                        }
                    }
                )
            }
            composable<Screen.NewPassword> {
                NewPasswordScreen(
                    navigateBack = {
                        navigationStateRepository.updateDestination(NavigationType.Back)
                    },
                    navigateToLoginScreen = {
                        navigationStateRepository.updateDestination(NavigationType.ClearBackStack(
                            Screen.Login))
                    }
                )
            }
        }

        composable<Screen.OnBoarding> {
            OnBoardingScreen(
                navigateToNextScreen = {
                    navigationStateRepository.updateDestination(NavigationType.ClearBackStack(
                        SubGraph.Main))
                }
            )
        }

        navigation<SubGraph.Main>(startDestination = Screen.Home) {
            composable<Screen.Home> {
                HomeScreen()
            }
            composable<Screen.AIChat> {
                Text(text = "AI Chat")
            }
            composable<Screen.Sessions> {
                Text(text = "Sessions")
            }
            composable<Screen.Doctors> {
                Text(text = "Doctors")
            }
        }
    }
}