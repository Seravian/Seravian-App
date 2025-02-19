package com.greenvenom.auth.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.greenvenom.auth.R
import com.greenvenom.ui.theme.AppTheme
import com.greenvenom.ui.theme.bluePrimary

@Composable
fun AuthHeader(
    title: String,
    isLoginScreen: Boolean = false,
    isNavigationBackWanted: Boolean = true,
    navigateBack: () -> Unit = {},
    navigateToRegister: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
    ) {
        Image(
            painter = painterResource(id = R.drawable.stars),
            contentDescription = stringResource(R.string.stars),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier
                .fillMaxSize()
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 20.dp)
        ) {
            if (isNavigationBackWanted) {
                IconButton(onClick = { navigateBack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.white_arrow_back),
                        contentDescription = stringResource(R.string.back_button),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
            }
            Row(
                modifier = Modifier.padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.logo_image),
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = stringResource(R.string.app_name),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(5.dp)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 30.sp,
                    lineHeight = 40.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 30.dp, start = 5.dp)
            )

            if (isLoginScreen) {
                Row(modifier = Modifier.padding(top = 20.dp)) {
                    Text(
                        text = stringResource(R.string.don_t_have_an_account),
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = stringResource(R.string.sign_up),
                        color = bluePrimary,
                        modifier = Modifier
                            .padding(start = 3.dp)
                            .clickable(enabled = true) {
                                navigateToRegister()
                            }
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AuthHeaderPreview() {
    AppTheme {
        AuthHeader(
            title = stringResource(R.string.sign_in_to_your_account),
            isLoginScreen = true,
            isNavigationBackWanted = false
        )
    }
}