package com.seravian.seravianapp.features.auth.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.seravian.seravianapp.core.navigation.AppNavigator
import com.seravian.seravianapp.core.presentation.BaseScreen
import com.seravian.seravianapp.ui.theme.SeravianTheme

@Composable
fun LoginScreen(
    appNavigator: AppNavigator,
    modifier: Modifier = Modifier
) {
    BaseScreen<LoginViewModel> { viewModel ->
        LoginContents()
    }
}

@Composable
fun LoginContents(modifier: Modifier = Modifier) {

}

@Preview(showSystemUi = true)
@Composable
private fun LoginContentsPreview() {
    SeravianTheme {
        LoginContents()
    }
}