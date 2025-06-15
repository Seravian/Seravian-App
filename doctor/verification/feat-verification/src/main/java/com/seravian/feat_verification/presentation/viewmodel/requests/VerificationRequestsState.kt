package com.seravian.feat_verification.presentation.viewmodel.requests

import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_verification.domain.Verification

data class VerificationRequestsState(
    val verificationRequests: List<Verification> = emptyList(),
    val requestsFetchingResult: NetworkResult<List<Verification>, NetworkError>? = null,
    val requestSendingResult: NetworkResult<Long, NetworkError>? = null
)
