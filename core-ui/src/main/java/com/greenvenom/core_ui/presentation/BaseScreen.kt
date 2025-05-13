package com.greenvenom.core_ui.presentation

import androidx.compose.runtime.Composable
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.greenvenom.core_ui.components.ErrorDialog
import com.greenvenom.core_ui.components.LoadingDialog
import org.koin.androidx.compose.koinViewModel

@Composable
inline fun<reified VM: BaseViewModel> BaseScreen(
    crossinline onCreateAction: (viewModel: VM) -> Unit = {},
    crossinline onStartAction: (viewModel: VM) -> Unit = {},
    crossinline onStopAction: (viewModel: VM) -> Unit = {},
    crossinline onDestroyAction: (viewModel: VM) -> Unit = {},
    crossinline onPhysicalBack: (viewModel: VM) -> Unit = {},
    modifier: Modifier = Modifier,
    enableCustomBack: Boolean = true,
    enableLifecycleObservation: Boolean = false,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    content: @Composable (viewModel: VM) -> Unit
) {
    val viewModel: VM = koinViewModel()
    val baseState = viewModel.baseState.collectAsState()

    if (enableLifecycleObservation) {
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        onCreateAction(viewModel)
                    }
                    Lifecycle.Event.ON_START -> {
                        onStartAction(viewModel)
                    }
                    Lifecycle.Event.ON_STOP -> {
                        onStopAction(viewModel)
                    }
                    Lifecycle.Event.ON_DESTROY -> {
                        onDestroyAction(viewModel)
                    }
                    else -> { /* Ignore other events */ }
                }
            }

            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }

    if (enableCustomBack) {
        BackHandler {
            onPhysicalBack(viewModel)
        }
    }

    content(viewModel)
    LoadingDialog(
        isLoading = baseState.value.isLoading
    )
    ErrorDialog(
        errorMessage = baseState.value.errorMessage,
        dismissAction = viewModel::hideErrorMessage
    )
}