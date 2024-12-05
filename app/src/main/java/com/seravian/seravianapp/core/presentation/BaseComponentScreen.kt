package com.seravian.seravianapp.core.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.seravian.seravianapp.common.components.ErrorDialog
import com.seravian.seravianapp.common.components.LoadingDialog

@Composable
inline fun<reified VM: BaseViewModel> BaseComposableScreen(
    content:@Composable (viewModel:VM)->Unit
) {
    val viewModel:VM = hiltViewModel()

    content(viewModel)
    LoadingDialog(showLoadingState = viewModel.loadingState)
    ErrorDialog(errorMessage = viewModel.dialogState)
}