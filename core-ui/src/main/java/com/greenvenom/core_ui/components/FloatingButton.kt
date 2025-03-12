package com.greenvenom.core_ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.greenvenom.core_ui.R
import com.greenvenom.core_ui.theme.AppTheme

@Composable
fun FloatingButton(
    isVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
        content = {
            Box(modifier = modifier) {
                FloatingActionButton(
                    onClick = { onClick() },
                    content = {
                        Icon(
                            painter = painterResource(R.drawable.add_ic),
                            contentDescription = stringResource(R.string.add)
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    )
}

@PreviewLightDark
@Composable
fun FloatingButtonPreview() {
    AppTheme {
        FloatingButton(
            isVisible = true,
            onClick = {}
        )
    }
}