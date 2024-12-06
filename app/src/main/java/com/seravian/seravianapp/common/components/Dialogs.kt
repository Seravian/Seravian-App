package com.seravian.seravianapp.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.seravian.seravianapp.R
import com.seravian.seravianapp.ui.theme.SeravianTheme
import com.seravian.seravianapp.ui.theme.bluePrimary
import com.seravian.seravianapp.ui.theme.transparentBlack

@Composable
fun LoadingDialog(
    showLoadingDialog: State<Boolean>,
    modifier: Modifier = Modifier
) {
    if (showLoadingDialog.value) {
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

@Preview(showSystemUi = true)
@Composable
private fun LoadingDialogPreview() {
    val showLoading = remember {
        mutableStateOf(true)
    }
    SeravianTheme {
        LoadingDialog(
            showLoadingDialog = showLoading
        )
    }
}

@Composable
fun ErrorDialog(
    errorMessage:State<String>,
    dismissAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    if(errorMessage.value.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = dismissAction,
            confirmButton = {
                TextButton(onClick = dismissAction) {
                    Text(
                        text = stringResource(id = R.string.ok),
                        color = bluePrimary
                    )
                }
            },
            text = { Text(text = errorMessage.value) }
        )
    }
}

@PreviewLightDark
@Composable
private fun ErrorDialogPreview() {
    val errorState = remember {
        mutableStateOf("Something went wrong")
    }
    SeravianTheme {
        ErrorDialog(
            errorMessage = errorState,
            dismissAction = {  }
        )
    }
}