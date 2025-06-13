package com.seravian.feat_verification.presentation.viewmodel.requests

import androidx.lifecycle.viewModelScope
import com.greenvenom.core_network.data.map
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_ui.presentation.BaseViewModel
import com.seravian.core_verification.data.dto.request.VerificationRequest
import com.seravian.feat_verification.domain.repo.VerificationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VerificationRequestsViewModel(
    private val verificationRepository: VerificationRepository
): BaseViewModel() {
    private val _verificationRequestsState = MutableStateFlow(VerificationRequestsState())
    val verificationRequestsState = _verificationRequestsState.asStateFlow()

    init {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                verificationRepository.getVerificationRequests()
            }

            result.onSuccess {
                _verificationRequestsState.update {
                    it.copy(
                        verificationRequests = it.verificationRequests,
                    )
                }
            }
            _verificationRequestsState.update {
                it.copy(
                    requestsFetchingResult = result.map {  }
                )
            }
        }
    }

    fun requestsAction(action: VerificationRequestsAction) {
        when (action) {
            is VerificationRequestsAction.SendVerificationRequest -> {
                sendVerificationRequest(
                    VerificationRequest(
                        doctorTitle = action.doctorTitle,
                        description = action.description,
                        sessionPrice = action.sessionPrice,
                        attachments = action.attachments
                    )
                )
            }

            VerificationRequestsAction.NavigateBack -> {}
        }
    }

    private fun sendVerificationRequest(request: VerificationRequest) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                verificationRepository.sendVerificationRequest(request)
            }

            _verificationRequestsState.update {
                it.copy(
                    requestsFetchingResult = result
                )
            }
        }
    }
}