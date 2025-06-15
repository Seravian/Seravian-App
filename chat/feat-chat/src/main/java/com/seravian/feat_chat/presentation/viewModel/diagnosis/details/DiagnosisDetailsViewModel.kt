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



    fun diagnosisDetailsAction(action: DiagnosisDetailsAction) {
        when(action) {

            DiagnosisDetailsAction.NavigateBack -> {

            }
            is DiagnosisDetailsAction.GetDiagnosis -> getDiagnosis(action.id)
            is DiagnosisDetailsAction.DeleteDiagnosis -> deleteDiagnosis(action.id)

        }
    }

    private fun getDiagnosis(id :Long) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                diagnosisRepository.getDiagnosis(
                    DiagnosisDetailsRequest(
                        id
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

    private fun deleteDiagnosis(id :Long) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                diagnosisRepository.deleteDiagnosis(
                    DiagnosisDeletionRequest(
                        id
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