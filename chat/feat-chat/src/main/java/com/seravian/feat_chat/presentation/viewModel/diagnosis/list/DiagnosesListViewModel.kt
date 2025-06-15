package com.seravian.feat_chat.presentation.viewModel.diagnosis.list

import androidx.lifecycle.viewModelScope
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.map
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_ui.presentation.BaseAction
import com.greenvenom.core_ui.presentation.BaseViewModel
import com.seravian.core_chat.data.dto.request.diagnosis.ChatDiagnosesRequest
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosesDeletionRequest
import com.seravian.feat_chat.data.repository.ChatBotStateRepository
import com.seravian.feat_chat.domain.repository.DiagnosisRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiagnosesListViewModel(
    private val diagnosisRepository: DiagnosisRepository,
    private val chatBotStateRepository: ChatBotStateRepository
): BaseViewModel() {
    private val _diagnosesListState = MutableStateFlow(DiagnosesListState())
    val diagnosesListState = _diagnosesListState.asStateFlow()

    var jobDiagnosesCollection: Job ?= null
    var jobConnectionStatusCollection: Job ?= null

    init {
        baseAction(BaseAction.ShowLoading)
        collectConnectionStatus()
    }

    fun diagnosesListAction(action: DiagnosesListAction) {
        when (action) {
            is DiagnosesListAction.NavigateToDiagnosisDetails -> {
                chatBotStateRepository.updateCurrentDiagnosis(action.diagnosis)
            }
            DiagnosesListAction.NavigateBack -> {
                stopDiagnosisCollections()
                viewModelScope.launch(Dispatchers.IO) {
                    chatBotStateRepository.stopConnection()
                }
            }

            DiagnosesListAction.DeleteAllCompletedDiagnoses -> deleteAllDiagnoses()
        }
    }

    private fun collectConnectionStatus() {
        jobConnectionStatusCollection = viewModelScope.launch {
            chatBotStateRepository.chatBotState.collect { newState ->
                when(newState.joinChatResult) {
                    is NetworkResult.Success -> {
                        getDiagnoses()
                    }

                    is NetworkResult.Error -> {
                        stopDiagnosisCollections()
                    }

                    null -> {
                        stopDiagnosisCollections()
                    }
                }
            }
        }
    }

    private fun getDiagnoses() {
        if (jobDiagnosesCollection != null) return

        jobDiagnosesCollection = viewModelScope.launch {
            diagnosisRepository.getDiagnoses(
                ChatDiagnosesRequest(chatBotStateRepository.chatBotState.value.currentChat?.id ?: "")
            ).collect { result ->
                result.onSuccess { diagnoses ->
                    _diagnosesListState.update {
                        it.copy(
                            diagnoses = diagnoses
                        )
                    }
                }

                _diagnosesListState.update {
                    it.copy(
                        fetchingDiagnosesResult = result.map {  }
                    )
                }
            }
        }
    }

    private fun deleteAllDiagnoses() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                diagnosisRepository.deleteDiagnoses(
                    DiagnosesDeletionRequest(
                        chatBotStateRepository.chatBotState.value.currentChat?.id ?: ""
                    )
                )
            }
            _diagnosesListState.update {
                it.copy(
                    deletingDiagnosesResult = result
                )
            }
        }
    }

    private fun stopDiagnosisCollections() {
        jobDiagnosesCollection?.cancel()
        jobDiagnosesCollection = null
    }
}