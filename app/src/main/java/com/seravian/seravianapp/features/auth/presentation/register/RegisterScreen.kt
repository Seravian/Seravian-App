package com.seravian.seravianapp.features.auth.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seravian.seravianapp.R
import com.seravian.seravianapp.core.navigation.AppNavigator
import com.seravian.seravianapp.core.presentation.BaseScreen
import com.seravian.seravianapp.ui.theme.AuthBlackColor
import com.seravian.seravianapp.ui.theme.SeravianTheme

@Composable
fun RegisterScreen(
    appNavigator: AppNavigator?,
    modifier: Modifier = Modifier
) {
    BaseScreen<RegisterViewModel> { viewModel ->
        val state = viewModel.state
        RegisterContents(
            appNavigator = appNavigator,
            state = state,
            action = viewModel::registerAction
        )
    }
}

@Composable
fun RegisterContents(
    appNavigator: AppNavigator?,
    state: RegisterInputState,
    action: (RegisterAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column() {
        Box(
            modifier = Modifier
                .background(color = AuthBlackColor)
                .fillMaxWidth()
                .fillMaxHeight(0.34f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.star),
                contentDescription = "Stars",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 20.dp)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                IconButton(onClick = { appNavigator?.navigateBack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.white_arrow_back_2),
                        contentDescription = "Back Button",
                        tint = colorResource(R.color.white),
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Text(
                    text = stringResource(R.string.register),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 50.sp,
                        lineHeight = 40.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = colorResource(R.color.white),
                    modifier = Modifier.padding(top = 30.dp, start = 5.dp)
                )
            }
        }
        Column() {

        }
    }
}

val mockState = RegisterInputState(
    usernameValidity = null,
    emailValidity = null,
    passwordValidity = null,
    confirmPasswordValidity = null
)

@Preview(showSystemUi = true)
@Composable
private fun RegisterContentsPreview() {
    SeravianTheme {
        RegisterContents(
            appNavigator = null,
            state = mockState,
            action = {  }
        )
    }
}