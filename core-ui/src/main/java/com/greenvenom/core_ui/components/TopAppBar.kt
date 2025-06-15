package com.greenvenom.core_ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.greenvenom.core_ui.R
import com.greenvenom.core_ui.theme.AppTheme

@Composable
fun TopAppBar(
    isVisible: Boolean,
    isSideDestination: Boolean,
    isActionEnabled: Boolean,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
    logo: Painter = painterResource(R.drawable.logo),
    title: String = stringResource(R.string.app_name),
    action: @Composable (RowScope.() -> Unit) = {}
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
        content = {
            TopBarContent(
                isSideDestination = isSideDestination,
                isActionEnabled = isActionEnabled,
                navigateBack = navigateBack,
                logo = logo,
                title = title,
                modifier = modifier,
                action = action
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarContent(
    isSideDestination: Boolean,
    isActionEnabled: Boolean,
    navigateBack: () -> Unit,
    logo: Painter,
    title: String,
    modifier: Modifier = Modifier,
    action: @Composable (RowScope.() -> Unit)
) {
    val bowlbyFontFamily = FontFamily(
        Font(R.font.bowlby_one_sc, weight = FontWeight.Normal)
    )

    val colorScheme = MaterialTheme.colorScheme

    TopAppBar(
        navigationIcon = {
            AnimatedVisibility(
                visible = isSideDestination,
                content = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.short_back_arrow),
                            contentDescription = stringResource(R.string.back_button),
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            )
        },
        actions = {
            if (isActionEnabled) {
                action()
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Logo
                Box(
                    modifier = Modifier
                        .size(48.dp)
                ) {
                    Image(
                        painter = logo,
                        contentDescription = "Logo",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                // Title
                Text(
                    text = title,
                    fontFamily = bowlbyFontFamily,
                    color = colorScheme.primaryContainer
                )
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun TopAppBarPreview() {
    AppTheme {
        TopBarContent(
            isSideDestination = true,
            isActionEnabled = true,
            navigateBack = {  },
            logo = painterResource(R.drawable.logo),
            title = "Testsdgd",
            action = {
                IconButton(onClick = {  }) {
                    Icon(
                        painter = painterResource(id = R.drawable.short_back_arrow),
                        contentDescription = stringResource(R.string.back_button),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                IconButton(onClick = {  }) {
                    Icon(
                        painter = painterResource(id = R.drawable.add_ic),
                        contentDescription = stringResource(R.string.back_button),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        )
    }
}