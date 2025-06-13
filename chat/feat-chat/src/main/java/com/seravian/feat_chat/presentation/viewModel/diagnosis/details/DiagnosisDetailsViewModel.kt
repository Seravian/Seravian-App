package com.seravian.feat_chat.presentation.viewModel.diagnosis.details

import com.greenvenom.core_ui.presentation.BaseViewModel
import com.seravian.feat_chat.data.repository.ChatBotStateRepository
import com.seravian.feat_chat.domain.repository.DiagnosisRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DiagnosisDetailsViewModel(
    private val diagnosisRepository: DiagnosisRepository,
    private val chatBotStateRepository: ChatBotStateRepository
): BaseViewModel() {
    private val _diagnosisDetailsState = MutableStateFlow(DiagnosisDetailsState())
    val diagnosisDetailsState = _diagnosisDetailsState.asStateFlow()

    init {
        _diagnosisDetailsState.update {
            it.copy(
                diagnosis = chatBotStateRepository.chatBotState.value.currentDiagnosis
            )
        }
    }

    fun diagnosisDetailsAction(action: DiagnosisDetailsAction) {
        when(action) {
            DiagnosisDetailsAction.NavigateBack -> {
                chatBotStateRepository.updateCurrentDiagnosis(null)
            }
        }
    }
}