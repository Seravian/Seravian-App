package com.seravian.seravianapp.features.auth.presentation.register

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.seravian.seravianapp.core.navigation.AppNavigator
import com.seravian.seravianapp.core.presentation.BaseScreen
import com.seravian.seravianapp.ui.theme.SeravianTheme

@Composable
fun RegisterScreen(
    appNavigator: AppNavigator,
    modifier: Modifier = Modifier
) {
    BaseScreen<RegisterViewModel> { viewModel ->
        RegisterContents()
    }
}

@Composable
fun RegisterContents(modifier: Modifier = Modifier) {

}

@Preview(showSystemUi = true)
@Composable
private fun RegisterContentsPreview() {
    SeravianTheme {
        RegisterContents()
    }
}