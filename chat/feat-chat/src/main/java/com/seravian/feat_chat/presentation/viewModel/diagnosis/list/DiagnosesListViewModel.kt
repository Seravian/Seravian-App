package com.seravian.feat_chat.presentation.viewModel.diagnosis.list

import androidx.lifecycle.viewModelScope
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_ui.presentation.BaseViewModel
import com.seravian.core_chat.data.dto.request.diagnosis.ChatDiagnosesRequest
import com.seravian.feat_chat.data.repository.ChatBotStateRepository
import com.seravian.feat_chat.domain.repository.DiagnosisRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DiagnosesListViewModel(
    private val diagnosisRepository: DiagnosisRepository,
    private val chatBotStateRepository: ChatBotStateRepository
): BaseViewModel() {
    private val _diagnosesListState = MutableStateFlow(DiagnosesListState())
    val diagnosesListState = _diagnosesListState.asStateFlow()

    var jobDiagnosesCollection: Job ?= null

    init {
        getDiagnoses()
    }

    fun diagnosisListAction(action: DiagnosesListAction) {
        when (action) {
            is DiagnosesListAction.NavigateToDiagnosisDetails -> {
                chatBotStateRepository.updateCurrentDiagnosis(action.diagnosis)
            }
            DiagnosesListAction.NavigateBack -> {

            }
        }
    }

    private fun getDiagnoses() {
        if (jobDiagnosesCollection != null) return

        jobDiagnosesCollection = viewModelScope.launch {
            diagnosisRepository.getDiagnoses(
                ChatDiagnosesRequest(chatBotStateRepository.chatBotState.value.currentChat?.id ?: "")
            ).collect { result ->
                result.onSuccess {
                    _diagnosesListState.update {
                        it.copy(
                            diagnoses = it.diagnoses
                        )
                    }
                }

                _diagnosesListState.update {
                    it.copy(
                        fetchingDiagnosesResult = result
                    )
                }
            }
        }
    }
}