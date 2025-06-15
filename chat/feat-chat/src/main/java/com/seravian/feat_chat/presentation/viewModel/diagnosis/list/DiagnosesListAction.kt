package com.seravian.feat_chat.presentation.viewModel.diagnosis.list

import com.seravian.core_chat.domain.models.Diagnosis

sealed interface DiagnosesListAction {
    data class NavigateToDiagnosisDetails(val diagnosis: Diagnosis) : DiagnosesListAction
    data object DeleteAllCompletedDiagnoses : DiagnosesListAction
    data object NavigateBack : DiagnosesListAction
}