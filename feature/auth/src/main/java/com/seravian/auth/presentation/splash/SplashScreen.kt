package com.seravian.auth.presentation.splash

import androidx.compose.runtime.Composable

@Composable
fun SplashScreen(
    navigateToLogin: () -> Unit
) {
    navigateToLogin()
}