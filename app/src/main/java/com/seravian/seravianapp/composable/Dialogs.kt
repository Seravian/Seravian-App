package com.seravian.seravianapp.composable

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.seravian.seravianapp.R
import com.seravian.seravianapp.ui.theme.bluePrimary

@Composable
fun LoadingDialog(showLoadingState: MutableState<Boolean>, modifier: Modifier = Modifier) {
    if (showLoadingState.value) {
        Dialog(onDismissRequest = { showLoadingState.value = false }) {
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

@Preview(showBackground = true,)
@Composable
private fun LoadingDialogPreview() {
    val showLoading = remember {
        mutableStateOf(true)
    }
    LoadingDialog(showLoadingState = showLoading)
}

@Composable
fun ErrorDialog(errorMessage:MutableState<String>,modifier: Modifier = Modifier) {
    if(errorMessage.value.isNotEmpty())
        AlertDialog(onDismissRequest = { errorMessage.value = "" }, confirmButton = { TextButton(
            onClick = { errorMessage.value = "" }) {
            Text(text = stringResource(id = R.string.ok), color = bluePrimary)
        } }, text = {
            Text(text = errorMessage.value)
        })
}

@Preview(showBackground = true,)
@Composable
private fun ErrorDialogPreview() {
    val errorState = remember {
        mutableStateOf("Something went wrong")
    }
    ErrorDialog(errorMessage = errorState)
}