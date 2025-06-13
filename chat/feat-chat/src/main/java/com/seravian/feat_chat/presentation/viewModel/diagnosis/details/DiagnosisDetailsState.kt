package com.seravian.feat_chat.presentation.viewModel.diagnosis.details

import androidx.compose.runtime.Immutable
import com.seravian.core_chat.domain.models.Diagnosis

@Immutable
data class DiagnosisDetailsState(
    val diagnosis: Diagnosis? = null
)
