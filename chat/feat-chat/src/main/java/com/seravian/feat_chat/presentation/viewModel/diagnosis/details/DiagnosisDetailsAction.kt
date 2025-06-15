package com.seravian.feat_chat.presentation.viewModel.diagnosis.details

sealed interface DiagnosisDetailsAction {
    data class DeleteDiagnosis(val id :Long) : DiagnosisDetailsAction
    data object NavigateBack : DiagnosisDetailsAction
    data class GetDiagnosis(val id:Long) : DiagnosisDetailsAction
}