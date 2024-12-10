package com.seravian.seravianapp.features.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.seravian.seravianapp.core.navigation.AppDestination
import com.seravian.seravianapp.core.navigation.AppNavigator
import com.seravian.seravianapp.features.auth.presentation.login.LoginScreen
import com.seravian.seravianapp.features.auth.presentation.register.RegisterScreen

fun NavGraphBuilder.authNavGraph(appNavigator: AppNavigator?) {
    composable<AppDestination.Login> {
        LoginScreen(appNavigator)
    }
    composable<AppDestination.Register> {
        RegisterScreen(appNavigator)
    }
    composable<AppDestination.ForgotPassword> {

    }
}