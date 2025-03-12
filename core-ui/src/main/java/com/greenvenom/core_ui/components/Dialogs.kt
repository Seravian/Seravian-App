package com.greenvenom.core_ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.greenvenom.core_ui.R
import com.greenvenom.core_ui.theme.AppTheme

@Composable
fun LoadingDialog(
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        Box(
            modifier = modifier
                .background(color = Color.Black.copy(alpha = 0.5f))
                .fillMaxSize()
        ) {
            Dialog(
                onDismissRequest = {  },
                properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun ErrorDialog(
    errorMessage: String,
    dismissAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    if(errorMessage.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = {
                dismissAction()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        dismissAction()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.dismiss),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            text = {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            modifier = modifier
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoadingDialogPreview() {
    LoadingDialog(
        isLoading = true
    )
}

@PreviewLightDark
@Composable
private fun ErrorDialogPreview() {
    AppTheme {
        ErrorDialog(
            errorMessage = "Something wrong happened",
            dismissAction = {  }
        )
    }
}