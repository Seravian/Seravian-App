package com.greenvenom.core_ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.greenvenom.core_ui.R
import com.greenvenom.core_ui.theme.AppTheme

@Composable
fun FloatingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
        .padding(12.dp)
    ) {
        FloatingActionButton(
            onClick = { onClick() },
            content = {
                Icon(
                    painter = painterResource(R.drawable.add_ic),
                    contentDescription = stringResource(R.string.add)
                )
            },
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    }
}

@PreviewLightDark
@Composable
fun FloatingButtonPreview() {
    AppTheme {
        FloatingButton(onClick = {})
    }
}