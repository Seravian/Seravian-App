package com.seravian.seravianapp.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.seravian.seravianapp.R
import com.seravian.seravianapp.ui.theme.bluePrimary
import kotlinx.coroutines.flow.StateFlow

@Composable
fun LoadingDialog(
    showLoadingState: State<Boolean>,
    dismissAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (showLoadingState.value) {
        Dialog(onDismissRequest = dismissAction) {
            Box(modifier = Modifier
                .size(100.dp)
                .background(Color.White, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(color = bluePrimary)
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun LoadingDialogPreview() {
    val showLoading = remember {
        mutableStateOf(true)
    }
    LoadingDialog(
        showLoadingState = showLoading,
        dismissAction = {  }
    )
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
    ErrorDialog(
        errorMessage = errorState,
        dismissAction = {  }
    )
}