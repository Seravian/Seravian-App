package com.seravian.seravianapp.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.seravian.seravianapp.common.components.ErrorDialog
import com.seravian.seravianapp.common.components.LoadingDialog
import org.koin.androidx.compose.koinViewModel

@Composable
inline fun<reified VM: BaseScreenViewModel> BaseScreen(
    content: @Composable (viewModel: VM) -> Unit,
) {
    val viewModel: VM = koinViewModel()
    val showLoadingDialog = viewModel.loadingState.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()

    content(viewModel)
    LoadingDialog(
        showLoadingDialog = showLoadingDialog
    )
    ErrorDialog(
        errorMessage = errorMessage,
        dismissAction = viewModel::hideErrorMessage
    )
}