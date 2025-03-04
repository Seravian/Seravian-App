package com.greenvenom.core_ui.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.greenvenom.core_ui.components.ErrorDialog
import com.greenvenom.core_ui.components.LoadingDialog
import org.koin.androidx.compose.koinViewModel

@Composable
inline fun<reified VM: BaseViewModel> BaseScreen(
    content: @Composable (viewModel: VM) -> Unit,
) {
    val viewModel: VM = koinViewModel()
    val baseState = viewModel.baseState.collectAsState()

    content(viewModel)
    LoadingDialog(
        isLoading = baseState.value.isLoading
    )
    ErrorDialog(
        errorMessage = baseState.value.errorMessage,
        dismissAction = viewModel::hideErrorMessage
    )
}