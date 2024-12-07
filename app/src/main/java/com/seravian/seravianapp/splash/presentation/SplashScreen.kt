package com.seravian.seravianapp.splash.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.seravian.seravianapp.core.navigation.AppDestination
import com.seravian.seravianapp.core.navigation.AppNavigator
import com.seravian.seravianapp.core.presentation.BaseScreen
import com.seravian.seravianapp.ui.theme.SeravianTheme

@Composable
fun SplashScreen(
    appNavigator: AppNavigator,
    modifier: Modifier = Modifier
) {
    BaseScreen<SplashViewModel> { viewModel ->
        SplashContents()
        Log.d("test", appNavigator.getCurrentDestination().toString())

        appNavigator.navigateTo(AppDestination.Login)
    }
}

@Composable
fun SplashContents(modifier: Modifier = Modifier) {

}

@Preview(showSystemUi = true)
@Composable
private fun SplashContentsPreview() {
    SeravianTheme {
        SplashContents()
    }
}