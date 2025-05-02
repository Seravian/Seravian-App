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
import androidx.navigation.toRoute
import com.greenvenom.core_navigation.data.NavigationType
import com.greenvenom.core_navigation.data.repository.NavigationStateRepository
import com.greenvenom.core_navigation.utils.AppNavigator
import com.greenvenom.core_network.domain.SessionDestinations
import com.greenvenom.core_network.domain.repository.SessionRepository
import com.greenvenom.feat_auth.presentation.login.LoginScreen
import com.greenvenom.feat_auth.presentation.otp.OtpScreen
import com.greenvenom.feat_auth.presentation.register.RegisterScreen
import com.greenvenom.feat_auth.presentation.reset_password.screens.NewPasswordScreen
import com.greenvenom.feat_auth.presentation.reset_password.screens.VerifyEmailScreen
import com.greenvenom.feat_auth.presentation.splash.SplashScreen
import com.seravian.feat_navigation.routes.Screen
import com.seravian.feat_navigation.routes.SubGraph
import com.seravian.feat_home.presentation.HomeScreen
import com.greenvenom.feat_onboarding.presentation.screens.OnBoardingScreen
import com.seravian.feat_chat.presentation.screen.ChatListScreen
import com.seravian.feat_chat.presentation.screen.ChatScreen
import com.seravian.feat_profile.presentation.screen.ProfileScreen
import com.seravian.seravianapp.navigation.utils.SessionDestinationHandler
import org.koin.compose.koinInject

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val appNavigator = koinInject<AppNavigator>()
    val navigationStateRepository = koinInject<NavigationStateRepository>()
    val navigationState by navigationStateRepository.navigationState.collectAsStateWithLifecycle()
    val sessionRepository = koinInject<SessionRepository>()
    val destinationHandler = koinInject<SessionDestinationHandler>()

    appNavigator.config(
        returnedDestination = Screen::class,
        navController = rememberNavController()
    )

    NavHost(
        navController = appNavigator.navController,
        startDestination = Screen.Splash,
        modifier = modifier
    ) {
        composable<Screen.Splash> {
            SplashScreen(
                onStart = {
                    sessionRepository.collectSessionStatus()
                    destinationHandler.collectSessionDestinations()
                }
            )
        }

        navigation<SubGraph.Auth>(startDestination = Screen.Login) {
            composable<Screen.Login> {
                LoginScreen(
                    navigateToRegisterScreen = {
                        navigationStateRepository.navigate(
                            NavigationType.Standard(Screen.Register)
                        )
                    },
                    navigateToEmailVerificationScreen = {
                        navigationStateRepository.navigate(
                            NavigationType.Standard(Screen.VerifyEmail)
                        )
                    },
                    navigateToOTPScreen = {
                        navigationStateRepository.navigate(
                            NavigationType.Standard(Screen.OTP)
                        )
                    }
                )
            }
            composable<Screen.Register> {
                RegisterScreen(
                    navigateBack = {
                        navigationStateRepository.navigate(NavigationType.Back)
                    },
                    navigateToAccountVerificationScreen = {
                        navigationStateRepository.navigate(
                            NavigationType.Standard(Screen.OTP)
                        )
                    }
                )
            }
            composable<Screen.VerifyEmail> {
                VerifyEmailScreen(
                    navigateBack = {
                        navigationStateRepository.navigate(NavigationType.Back)
                    },
                    navigateToOtpScreen = {
                        navigationStateRepository.navigate(
                            NavigationType.Standard(Screen.OTP)
                        )
                    }
                )
            }
            composable<Screen.OTP> {
                OtpScreen(
                    navigateBack = {
                        navigationStateRepository.navigate(NavigationType.Back)
                    },
                    navigateToNextScreen = {
                        when (navigationState.previousDestination) {
                            is Screen.Login -> {
                                navigationStateRepository.navigate(
                                    NavigationType.ClearBackStack(Screen.Login)
                                )
                            }
                            is Screen.Register -> {
                                navigationStateRepository.navigate(
                                    NavigationType.ClearBackStack(Screen.Login)
                                )
                            }
                            is Screen.VerifyEmail -> {
                                navigationStateRepository.navigate(
                                    NavigationType.ClearBackStack(Screen.NewPassword)
                                )
                            }
                        }
                    }
                )
            }
            composable<Screen.NewPassword> {
                NewPasswordScreen(
                    navigateBack = {
                        navigationStateRepository.navigate(NavigationType.Back)
                    },
                    navigateToLoginScreen = {
                        navigationStateRepository.navigate(
                            NavigationType.ClearBackStack(Screen.Login)
                        )
                    }
                )
            }
        }

        navigation<SubGraph.OnBoarding>(startDestination = Screen.OnBoarding) {
            composable<Screen.OnBoarding> {
                OnBoardingScreen(
                    navigateToNextScreen = {
                        navigationStateRepository.navigate(
                            NavigationType.ClearBackStack(SubGraph.Main)
                        )
                    }
                )
            }
        }

        navigation<SubGraph.Main>(startDestination = Screen.Home) {
            composable<Screen.Home> {
                HomeScreen()
            }
            composable<Screen.ChatsList> {
                ChatListScreen(
                    navigateToChat = { chatId ->
                        navigationStateRepository.navigate(
                            NavigationType.Standard(Screen.Chat(chatId))
                        )
                    }
                )
            }
            composable<Screen.Sessions> {
                Text(text = "Sessions")
            }
            composable<Screen.Doctors> {
                Text(text = "Doctors")
            }
            composable<Screen.Chat> {
                val args = it.toRoute<Screen.Chat>()
                ChatScreen(args.chatId)
            }
            composable<Screen.Profile> {
                ProfileScreen(
                    onLogoutNavigate = {
                        navigationStateRepository.navigate(
                            NavigationType.ClearBackStack(Screen.Login)
                        )
                    }
                )
            }
        }
    }
}