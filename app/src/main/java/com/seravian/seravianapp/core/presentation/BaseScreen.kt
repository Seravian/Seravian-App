package com.seravian.seravianapp.core.presentation

import androidx.compose.runtime.Composable
import com.seravian.seravianapp.common.components.ErrorDialog
import com.seravian.seravianapp.common.components.LoadingDialog
import org.koin.androidx.compose.koinViewModel

@Composable
inline fun<reified VM: BaseViewModel> BaseScreen(
    content: @Composable (viewModel: VM) -> Unit,
) {
    val viewModel: VM = koinViewModel()
    val state = viewModel.baseState

    content(viewModel)
    LoadingDialog(
        state = state.value
    )
    ErrorDialog(
        state = state.value,
        onAction = viewModel::onBaseAction
    )
}