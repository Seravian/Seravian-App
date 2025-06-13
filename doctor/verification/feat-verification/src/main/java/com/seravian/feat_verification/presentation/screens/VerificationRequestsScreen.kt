package com.seravian.feat_verification.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.greenvenom.core_network.data.onError
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_network.utils.toString
import com.greenvenom.core_ui.components.TopAppBar
import com.greenvenom.core_ui.presentation.BaseAction
import com.greenvenom.core_ui.presentation.BaseScreen
import com.greenvenom.core_ui.theme.AppTheme
import com.seravian.feat_verification.R
import com.seravian.feat_verification.presentation.components.CreateVerificationRequestContent
import com.seravian.feat_verification.presentation.components.VerificationRequestItem
import com.seravian.feat_verification.presentation.models.toVerificationUI
import com.seravian.feat_verification.presentation.viewmodel.requests.VerificationRequestsAction
import com.seravian.feat_verification.presentation.viewmodel.requests.VerificationRequestsState
import com.seravian.feat_verification.presentation.viewmodel.requests.VerificationRequestsViewModel

@Composable
fun VerificationRequestsScreen(
    navigateBack: () -> Unit,
    navigateToRequestDetails: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BaseScreen<VerificationRequestsViewModel>(
        onPhysicalBack = { viewModel ->
            viewModel.requestsAction(VerificationRequestsAction.NavigateBack)
            navigateBack()
        }
    ) { viewModel ->
        val state by viewModel.verificationRequestsState.collectAsStateWithLifecycle()

        VerificationRequestsContent(
            state = state,
            requestsAction = {
                when(it) {
                    VerificationRequestsAction.NavigateBack -> navigateBack()
                    else -> {}
                }
                viewModel.requestsAction(it)
            },
            baseAction = viewModel::baseAction,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VerificationRequestsContent(
    state: VerificationRequestsState,
    requestsAction: (VerificationRequestsAction) -> Unit,
    baseAction: (BaseAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()

    state.requestsFetchingResult
        ?.onSuccess {
            baseAction(BaseAction.HideLoading)
        }
        ?.onError {
            baseAction(BaseAction.HideLoading)
            baseAction(BaseAction.ShowErrorMessage(it.errorType?.toString(context) ?: ""))
        }

    Scaffold(
        topBar = {
            TopAppBar(
                isVisible = true,
                isActionEnabled = false,
                isSideDestination = true,
                navigateBack = { requestsAction(VerificationRequestsAction.NavigateBack) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
            ) {
                Icon(
                    painter = painterResource(R.drawable.add_ic),
                    contentDescription = stringResource(R.string.create_new_verification_request)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = state.verificationRequests,
                    key = { request -> request.id }
                ) { request ->
                    VerificationRequestItem(
                        request = request.toVerificationUI(),
                        onClick = {

                        }
                    )
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState,
            modifier = Modifier.fillMaxHeight(0.9f)
        ) {
            CreateVerificationRequestContent(
                onDismiss = { showBottomSheet = false },
                onSubmit = { action ->
                    requestsAction(action)
                    showBottomSheet = false
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewVerificationRequestsScreen() {
    AppTheme {
        VerificationRequestsContent(
            state = VerificationRequestsState(),
            requestsAction = {},
            baseAction = {}
        )
    }
}