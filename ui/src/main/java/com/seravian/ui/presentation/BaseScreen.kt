package com.seravian.ui.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.seravian.ui.components.ErrorDialog
import com.seravian.ui.components.LoadingDialog
import org.koin.androidx.compose.koinViewModel

@Composable
inline fun<reified VM: BaseViewModel> BaseScreen(
    content: @Composable (viewModel: VM) -> Unit,
) {
    val viewModel: VM = koinViewModel()
    val state = viewModel.baseState.collectAsState()

    content(viewModel)
    LoadingDialog(
        isLoading = state.value.isLoading
    )
    ErrorDialog(
        errorMessage = state.value.errorMessage,
        dismissAction = viewModel::hideErrorMessage
    )
}