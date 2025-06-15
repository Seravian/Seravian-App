package com.seravian.feat_chat.presentation.viewModel.diagnosis.details

import androidx.compose.runtime.Immutable
import com.seravian.core_network.data.EmptyResult
import com.seravian.core_network.data.NetworkError
import com.seravian.core_chat.domain.models.Diagnosis

@Immutable
data class DiagnosisDetailsState(
    val diagnosis: Diagnosis? = null,
    val diagnosisFetchingResult: EmptyResult<NetworkError>? = null,
    val deletionResult: EmptyResult<NetworkError>? = null
)
