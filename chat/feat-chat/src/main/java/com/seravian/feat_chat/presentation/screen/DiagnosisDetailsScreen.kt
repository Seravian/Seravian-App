package com.seravian.feat_chat.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seravian.core_network.data.onError
import com.seravian.core_network.data.onSuccess
import com.seravian.core_network.utils.toString
import com.seravian.core_ui.components.TopAppBar
import com.seravian.core_ui.presentation.BaseAction
import com.seravian.core_ui.presentation.BaseScreen
import com.seravian.core_ui.theme.AppTheme
import com.seravian.core_chat.domain.models.Diagnosis
import com.seravian.feat_chat.R
import com.seravian.feat_chat.presentation.components.diagnosis.DiagnosisInfoCard
import com.seravian.feat_chat.presentation.models.toDetailsUI
import com.seravian.feat_chat.presentation.viewModel.diagnosis.details.DiagnosisDetailsAction
import com.seravian.feat_chat.presentation.viewModel.diagnosis.details.DiagnosisDetailsState
import com.seravian.feat_chat.presentation.viewModel.diagnosis.details.DiagnosisDetailsViewModel

@Composable
fun DiagnosisDetailsScreen(
    diagnosisId: Long,
    navigateBack: () -> Unit
) {
    BaseScreen<DiagnosisDetailsViewModel>(
        onPhysicalBack = { viewModel ->
            viewModel.diagnosisDetailsAction(DiagnosisDetailsAction.NavigateBack)
            navigateBack()
        },
    ) { viewModel ->
        val state by viewModel.diagnosisDetailsState.collectAsStateWithLifecycle()
        viewModel.diagnosisDetailsAction(DiagnosisDetailsAction.GetDiagnosis(diagnosisId))
        DiagnosisDetailsContent(
            state = state,
            detailsAction = {
                when (it) {
                    DiagnosisDetailsAction.NavigateBack -> navigateBack()
                    else -> {}
                }
                viewModel.diagnosisDetailsAction(it)
            },
            baseAction = viewModel::baseAction,
            diagnosisId = diagnosisId
        )
    }
}

@Composable
private fun DiagnosisDetailsContent(
    state: DiagnosisDetailsState,
    detailsAction: (DiagnosisDetailsAction) -> Unit,
    baseAction: (BaseAction) -> Unit,
    diagnosisId:Long
) {
    val context = LocalContext.current

    state.diagnosisFetchingResult
        ?.onSuccess {
            baseAction(BaseAction.HideLoading)
        }
        ?.onError {
            baseAction(
                BaseAction.ShowErrorMessage(
                it.errorType?.toString(context) ?: "",
                dismissAction = { detailsAction(DiagnosisDetailsAction.NavigateBack) }
            ))
        }

    state.deletionResult
        ?.onSuccess {
            baseAction(BaseAction.HideLoading)
            detailsAction(DiagnosisDetailsAction.NavigateBack)
        }
        ?.onError {
            baseAction(
                BaseAction.ShowErrorMessage(
                    it.errorType?.toString(context) ?: ""
                )
            )
        }

    Scaffold(
        topBar = {
            TopAppBar(
                isVisible = true,
                isActionEnabled = false,
                isSideDestination = true,
                title = stringResource(R.string.diagnoses_title),
                navigateBack = { detailsAction(DiagnosisDetailsAction.NavigateBack) }
            )
        }
    ) { innerPadding ->
        val diagnosis = state.diagnosis?.toDetailsUI()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.diagnosis_details),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold
                            )

                            diagnosis?.id?.let { id ->
                                Surface(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.number_of, id),
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 6.dp
                                        ),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_schedule),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(
                                    R.string.requested,
                                    diagnosis?.requestedAtUtc ?: ""
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        diagnosis?.completedAtUtc?.let { completedAt ->
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.completed, completedAt),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }

            // Main Content
            diagnosis?.let { diagnosisUI ->
                if (diagnosisUI.failureReason != null) {
                    // Show failure reason
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_error),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onErrorContainer,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = stringResource(R.string.diagnosis_failed),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = diagnosisUI.failureReason,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                } else {
                    // Show successful diagnosis details

                    // Diagnosed Problem
                    diagnosisUI.diagnosedProblem?.let { problem ->
                        item {
                            DiagnosisInfoCard(
                                title = stringResource(R.string.diagnosed_problem),
                                content = problem,
                                icon = painterResource(R.drawable.ic_medical_services),
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    // Reasoning
                    diagnosisUI.reasoning?.let { reasoning ->
                        item {
                            DiagnosisInfoCard(
                                title = stringResource(R.string.medical_reasoning),
                                content = reasoning,
                                icon = painterResource(R.drawable.ic_psychology),
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }

                    // Prescriptions
                    diagnosisUI.prescriptions?.let { prescriptions ->
                        if (prescriptions.isNotEmpty()) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(20.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.ic_local_pharmacy),
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(32.dp)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = stringResource(R.string.prescriptions),
                                                style = MaterialTheme.typography.titleMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))

                                        prescriptions.forEachIndexed { index, prescription ->
                                            Surface(
                                                modifier = Modifier.fillMaxWidth(),
                                                color = MaterialTheme.colorScheme.surface,
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(16.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Surface(
                                                        color = MaterialTheme.colorScheme.primary,
                                                        shape = CircleShape,
                                                        modifier = Modifier.size(24.dp)
                                                    ) {
                                                        Box(
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            Text(
                                                                text = stringResource(
                                                                    R.string.index,
                                                                    index + 1
                                                                ),
                                                                style = MaterialTheme.typography.labelSmall,
                                                                color = MaterialTheme.colorScheme.onPrimary,
                                                                fontWeight = FontWeight.Bold
                                                            )
                                                        }
                                                    }

                                                    Spacer(modifier = Modifier.width(12.dp))

                                                    Text(
                                                        text = prescription,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        color = MaterialTheme.colorScheme.onSurface,
                                                        lineHeight = 20.sp
                                                    )
                                                }
                                            }

                                            if (index < prescriptions.lastIndex) {
                                                Spacer(modifier = Modifier.height(8.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Delete Button
            item {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = {
                        baseAction(BaseAction.ShowLoading)
                        detailsAction(DiagnosisDetailsAction.DeleteDiagnosis(diagnosisId))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.delete_diagnosis),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview
@Composable
private fun PreviewDiagnosisDetailsContent() {
    AppTheme {
        DiagnosisDetailsContent(
            state = DiagnosisDetailsState(
                diagnosis = Diagnosis(
                    id = 1,
                    requestedAtUtc = "2023-06-05T14:30:40Z",
                    completedAtUtc = "2026-07-05T14:30:40Z",
                    diagnosedProblem = "Diagnosed Problem",
                    reasoning = "Reasoning",
                    prescriptions = listOf("Prescription 1", "Prescription 2", "Prescription 3"),
                    failureReason = null
                )
            ),
            detailsAction = {},
            baseAction = {},
            diagnosisId = 2
        )
    }
}