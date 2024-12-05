package com.seravian.seravianapp.base

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.base.BaseViewModel
import com.seravian.seravianapp.composable.ErrorDialog
import com.seravian.seravianapp.composable.LoadingDialog

@Composable
inline fun<reified VM: BaseViewModel> BaseComposableScreen(content:@Composable (viewModel:VM)->Unit) {
    val viewModel:VM = hiltViewModel()

    content(viewModel)
    LoadingDialog(showLoadingState = viewModel.loadingState)
    ErrorDialog(errorMessage = viewModel.dialogState)
}