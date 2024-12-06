package com.seravian.seravianapp.features.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.seravian.seravianapp.core.navigation.AppDestination
import com.seravian.seravianapp.core.navigation.AppNavigator
import com.seravian.seravianapp.features.auth.presentation.login.LoginScreen
import com.seravian.seravianapp.features.auth.presentation.register.RegisterScreen

fun NavGraphBuilder.authNavGraph(appNavigator: AppNavigator) {
    navigation<AppDestination>(
        startDestination = AppDestination.Login
    ) {
        composable<AppDestination.Login> {
            LoginScreen(appNavigator)
        }
        composable<AppDestination.Register> {
            RegisterScreen(appNavigator)
        }
    }
}