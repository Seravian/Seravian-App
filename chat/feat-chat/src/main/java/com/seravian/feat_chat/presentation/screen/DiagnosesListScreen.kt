package com.seravian.feat_chat.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.greenvenom.core_ui.presentation.BaseScreen
import com.seravian.feat_chat.presentation.viewModel.diagnosis.list.DiagnosesListAction
import com.seravian.feat_chat.presentation.viewModel.diagnosis.list.DiagnosesListViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.greenvenom.core_network.data.onError
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_ui.components.TopAppBar
import com.greenvenom.core_ui.presentation.BaseAction
import com.greenvenom.core_ui.theme.AppTheme
import com.seravian.core_chat.domain.models.Diagnosis
import com.seravian.feat_chat.R
import com.seravian.feat_chat.presentation.components.diagnosis.DeleteCompletedDiagnosesDialog
import com.seravian.feat_chat.presentation.components.diagnosis.DiagnosisCard
import com.seravian.feat_chat.presentation.models.toCardUI
import com.seravian.feat_chat.presentation.viewModel.diagnosis.list.DiagnosesListState

@Composable
fun DiagnosesListScreen(
    navigateToDiagnosisDetails: () -> Unit,
    navigateBack: () -> Unit
) {
    BaseScreen<DiagnosesListViewModel>(
        onPhysicalBack = { viewModel ->
            viewModel.diagnosesListAction(DiagnosesListAction.NavigateBack)
            navigateBack()
        },
    ) { viewModel ->
        val diagnosesListState by viewModel.diagnosesListState.collectAsStateWithLifecycle()

        DiagnosesListContent(
            state = diagnosesListState,
            listAction = {
                when (it) {
                    is DiagnosesListAction.NavigateToDiagnosisDetails -> navigateToDiagnosisDetails()
                    DiagnosesListAction.NavigateBack -> navigateBack()
                    else -> {}
                }
                viewModel.diagnosesListAction(it)
            },
            baseAction = viewModel::baseAction
        )
    }
}

@Composable
private fun DiagnosesListContent(
    state: DiagnosesListState,
    listAction: (DiagnosesListAction) -> Unit,
    baseAction: (BaseAction) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    state.fetchingDiagnosesResult
        ?.onSuccess {
            baseAction(BaseAction.HideLoading)
        }
        ?.onError {
            baseAction(BaseAction.HideLoading)
            baseAction(BaseAction.ShowErrorMessage(
                errorMessage = it.errorType?.toString() ?: "",
                dismissAction = { listAction(DiagnosesListAction.NavigateBack) }
            ))
        }

    state.deletingDiagnosesResult
        ?.onSuccess {
            baseAction(BaseAction.HideLoading)
        }
        ?.onError {
            baseAction(BaseAction.HideLoading)
            baseAction(BaseAction.ShowErrorMessage(
                errorMessage = it.errorType?.toString() ?: ""
            ))
        }

    Scaffold(
        topBar = {
            TopAppBar(
                isVisible = true,
                isActionEnabled = true,
                isSideDestination = true,
                title = stringResource(R.string.diagnoses_title),
                navigateBack = { listAction(DiagnosesListAction.NavigateBack) },
                action = {
                    IconButton(
                        onClick = { showDeleteDialog = true },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete),
                            contentDescription = stringResource(R.string.delete),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (state.diagnoses.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_medical_services),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.no_diagnoses_yet),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = stringResource(R.string.your_diagnoses_will_appear_here),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                items(
                    items = state.diagnoses,
                    key = { it.id }
                ) { diagnosis ->
                    DiagnosisCard(
                        diagnosis = diagnosis.toCardUI(),
                        onClick = {
                            listAction(DiagnosesListAction.NavigateToDiagnosisDetails(diagnosis))
                        }
                    )
                }

                // Bottom spacing
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    if (showDeleteDialog) {
        DeleteCompletedDiagnosesDialog(
            onConfirm = {
                showDeleteDialog = false
                baseAction(BaseAction.ShowLoading)
                listAction(DiagnosesListAction.DeleteAllCompletedDiagnoses)
            },
            onDismiss = {
                showDeleteDialog = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDiagnosesListContent() {
    AppTheme {
        DiagnosesListContent(
            state = DiagnosesListState(
                diagnoses = listOf(
                    Diagnosis(
                        id = 1,
                        requestedAtUtc = "2023-06-05T14:30:40Z",
                        completedAtUtc = "2026-07-05T14:30:40Z",
                        diagnosedProblem = "Diagnosed Problem",
                        reasoning = "Reasoning",
                        prescriptions = listOf("Prescription 1", "Prescription 2", "Prescription 3"),
                        failureReason = null,
                    )
                )
            ),
            listAction = {},
            baseAction = {}
        )
    }
}