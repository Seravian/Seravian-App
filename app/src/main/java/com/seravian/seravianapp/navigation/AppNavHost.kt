package com.seravian.seravianapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.greenvenom.core_navigation.data.repository.NavigationStateRepository
import com.greenvenom.core_navigation.utils.AppNavigator
import com.greenvenom.feat_auth.presentation.splash.SplashScreen
import com.greenvenom.feat_onboarding.presentation.screens.OnBoardingScreen
import com.seravian.feat_navigation.routes.Screen
import com.seravian.feat_navigation.routes.SubGraph
import com.seravian.seravianapp.navigation.graphs.authGraph
import com.seravian.seravianapp.navigation.graphs.doctorGraph
import com.seravian.seravianapp.navigation.graphs.patientGraph
import com.seravian.seravianapp.navigation.utils.SessionDestinationHandler
import org.koin.compose.koinInject

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val appNavigator = koinInject<AppNavigator>()
    val navigationRepository = koinInject<NavigationStateRepository>()
    val navigationState by navigationRepository.navigationState.collectAsStateWithLifecycle()
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
                    destinationHandler.collectSessionDestinations()
                }
            )
        }

        authGraph(navigationRepository::navigate, navigationState)

        navigation<SubGraph.OnBoarding>(startDestination = Screen.OnBoarding) {
            composable<Screen.OnBoarding> {
                OnBoardingScreen(
                    navigateToNextScreen = {

                    }
                )
            }
        }

        patientGraph(navigationRepository::navigate)

        doctorGraph(navigationRepository::navigate)
    }
}