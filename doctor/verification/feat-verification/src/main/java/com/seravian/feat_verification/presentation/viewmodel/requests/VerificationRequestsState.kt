package com.seravian.feat_verification.presentation.viewmodel.requests

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.seravian.core_verification.domain.Verification

data class VerificationRequestsState(
    val verificationRequests: List<Verification> = emptyList(),
    val requestsFetchingResult: EmptyResult<NetworkError>? = null
)
