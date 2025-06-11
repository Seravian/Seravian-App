package com.seravian.feat_verification.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.greenvenom.core_ui.components.TopAppBar
import com.greenvenom.core_ui.presentation.BaseAction
import com.greenvenom.core_ui.presentation.BaseScreen
import com.greenvenom.core_ui.theme.AppTheme
import com.seravian.core_verification.domain.utils.VerificationStatus
import com.seravian.feat_verification.R
import com.seravian.feat_verification.presentation.components.CreateVerificationRequestContent
import com.seravian.feat_verification.presentation.components.VerificationRequestItem
import com.seravian.feat_verification.presentation.viewmodel.requests.VerificationRequestsAction
import com.seravian.feat_verification.presentation.viewmodel.requests.VerificationRequestsState
import com.seravian.feat_verification.presentation.viewmodel.requests.VerificationRequestsViewModel

@Composable
fun VerificationRequestsScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseScreen<VerificationRequestsViewModel>(
        onPhysicalBack = { viewModel ->
            navigateBack()
        }
    ) { viewModel ->
        val state by viewModel.verificationRequestsState.collectAsStateWithLifecycle()

        VerificationRequestsContent(
            state = state,
            requestsAction = viewModel::requestsAction,
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
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            TopAppBar(
                isVisible = true,
                isActionEnabled = false,
                isSideDestination = false,
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
                items(5) { index ->
                    VerificationRequestItem(
                        title = "Verification Request #${index + 1}",
                        status = if (index % 3 == 0) VerificationStatus.PENDING
                        else if (index % 3 == 1) VerificationStatus.APPROVED
                        else VerificationStatus.REJECTED,
                        date = "June ${10 - index}, 2025"
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
                    // Handle the verification request submission
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