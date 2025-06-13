package com.seravian.feat_chat.presentation.viewModel.diagnosis.list

import androidx.compose.runtime.Immutable
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_chat.domain.models.Diagnosis

@Immutable
data class DiagnosesListState(
    val diagnoses: List<Diagnosis> = emptyList(),
    val fetchingDiagnosesResult: NetworkResult<List<Diagnosis>, NetworkError>? = null
)
