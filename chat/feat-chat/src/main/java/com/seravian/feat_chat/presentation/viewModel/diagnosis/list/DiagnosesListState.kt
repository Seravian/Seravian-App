package com.seravian.feat_chat.presentation.viewModel.diagnosis.list

import androidx.compose.runtime.Immutable
import com.seravian.core_network.data.EmptyResult
import com.seravian.core_network.data.NetworkError
import com.seravian.core_chat.domain.models.Diagnosis

@Immutable
data class DiagnosesListState(
    val diagnoses: List<Diagnosis> = emptyList(),
    val fetchingDiagnosesResult: EmptyResult<NetworkError>? = null,
    val deletingDiagnosesResult: EmptyResult<NetworkError>? = null
)
