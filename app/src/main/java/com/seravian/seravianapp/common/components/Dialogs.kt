package com.seravian.seravianapp.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.seravian.seravianapp.R
import com.seravian.seravianapp.core.presentation.BaseAction
import com.seravian.seravianapp.core.presentation.BaseState
import com.seravian.seravianapp.ui.theme.SeravianTheme
import com.seravian.seravianapp.ui.theme.bluePrimary
import com.seravian.seravianapp.ui.theme.transparentBlack

@Composable
fun LoadingDialog(
    state: BaseState,
    modifier: Modifier = Modifier
) {
    if (state.isLoading) {
        Box(
            modifier = modifier
                .background(color = transparentBlack)
                .fillMaxSize()
        ) {
            Dialog(
                onDismissRequest = {  },
                properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
            ) {
                CircularProgressIndicator(color = bluePrimary)
            }
        }
    }
}

@Composable
fun ErrorDialog(
    state: BaseState,
    onAction: (BaseAction) -> Unit,
    modifier: Modifier = Modifier
) {
    if(state.errorMessage.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = {
                onAction(BaseAction.HideErrorMessage)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onAction(BaseAction.HideErrorMessage)
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.ok),
                        color = bluePrimary
                    )
                }
            },
            text = { Text(text = state.errorMessage) }
        )
    }
}

val mockState = BaseState(isLoading = true, errorMessage = "Something went wrong")

@Preview(showSystemUi = true)
@Composable
private fun LoadingDialogPreview() {
    SeravianTheme {
        LoadingDialog(
            state = mockState
        )
    }
}

@PreviewLightDark
@Composable
private fun ErrorDialogPreview() {
    SeravianTheme {
        ErrorDialog(
            state = mockState,
            onAction = {  }
        )
    }
}