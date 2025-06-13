package com.seravian.feat_chat.presentation.viewModel.diagnosis.details

sealed interface DiagnosisDetailsAction {
    data object NavigateBack : DiagnosisDetailsAction
}