package com.seravian.seravianapp.features.auth.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.seravian.seravianapp.R
import com.seravian.seravianapp.core.navigation.AppDestination
import com.seravian.seravianapp.core.navigation.AppNavigator
import com.seravian.seravianapp.core.presentation.BaseScreen
import com.seravian.seravianapp.ui.theme.AuthBlackColor
import com.seravian.seravianapp.ui.theme.SeravianTheme
import com.seravian.seravianapp.ui.theme.bluePrimary

@Composable
fun LoginScreen(
    appNavigator: AppNavigator?,
    modifier: Modifier = Modifier
) {
    BaseScreen<LoginViewModel> { viewModel ->
        val state = viewModel.state
        LoginContents(
            appNavigator = appNavigator,
            state = state,
            action = viewModel::onAction
        )
    }
}

@Composable
fun LoginContents(
    appNavigator: AppNavigator?,
    state: LoginInputState,
    action: (LoginAction) -> Unit,
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
                Row {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = stringResource(R.string.logo_image),
                        modifier = Modifier.padding(5.dp)
                    )
                    Text(
                        text = stringResource(R.string.seravian),
                        color = colorResource(R.color.white),
                        modifier = Modifier.padding(5.dp)
                    )
                }
                Text(
                    text = stringResource(R.string.sign_in_into_your_account),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 30.sp,
                        lineHeight = 40.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = colorResource(R.color.white),
                    modifier = Modifier.padding(top = 30.dp, start = 5.dp)
                )

                Row(modifier = Modifier.padding(top = 20.dp)) {
                    Text(
                        text = stringResource(R.string.don_t_have_an_account),
                        color = colorResource(R.color.white),

                        )
                    Text(
                        text = stringResource(R.string.sign_up),
                        color = bluePrimary,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier
                            .padding(start = 3.dp)
                            .clickable(enabled = true) {
                                appNavigator?.navigateTo(AppDestination.Register)
                            }
                    )
                }
            }
        }
        Column() {

        }
    }
}

val mockState = LoginInputState(
    emailValidity = null,
    passwordValidity = null
)

@Preview(showSystemUi = true)
@Composable
private fun LoginContentsPreview() {
    SeravianTheme {
        LoginContents(
            appNavigator = null,
            state = mockState,
            action = {  }
        )
    }
}