package com.seravian.feat_chat.presentation.viewModel.diagnosis.details

import androidx.lifecycle.viewModelScope
import com.seravian.core_network.data.map
import com.seravian.core_network.data.onSuccess
import com.seravian.core_ui.presentation.BaseViewModel
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosisDeletionRequest
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosisDetailsRequest
import com.seravian.feat_chat.data.repository.ChatBotStateRepository
import com.seravian.feat_chat.domain.repository.DiagnosisRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiagnosisDetailsViewModel(
    private val diagnosisRepository: DiagnosisRepository,
    private val chatBotStateRepository: ChatBotStateRepository
): BaseViewModel() {
    private val _diagnosisDetailsState = MutableStateFlow(DiagnosisDetailsState())
    val diagnosisDetailsState = _diagnosisDetailsState.asStateFlow()

    init {
        getDiagnosis()
    }

    fun diagnosisDetailsAction(action: DiagnosisDetailsAction) {
        when(action) {
            DiagnosisDetailsAction.DeleteDiagnosis -> {
                deleteDiagnosis()
            }

            DiagnosisDetailsAction.NavigateBack -> {
                chatBotStateRepository.updateCurrentDiagnosis(null)
            }
        }
    }

    private fun getDiagnosis() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                diagnosisRepository.getDiagnosis(
                    DiagnosisDetailsRequest(
                        chatBotStateRepository.chatBotState.value.currentDiagnosis?.id ?: 0
                    )
                )
            }
            result.onSuccess { diagnosis ->
                _diagnosisDetailsState.update {
                    it.copy(
                        diagnosis = diagnosis
                    )
                }
            }

            _diagnosisDetailsState.update {
                it.copy(
                    diagnosisFetchingResult = result.map {  }
                )
            }
        }
    }

    private fun deleteDiagnosis() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                diagnosisRepository.deleteDiagnosis(
                    DiagnosisDeletionRequest(
                        chatBotStateRepository.chatBotState.value.currentDiagnosis?.id ?: 0
                    )
                )
            }

            _diagnosisDetailsState.update {
                it.copy(
                    deletionResult = result
                )
            }
        }
    }
}