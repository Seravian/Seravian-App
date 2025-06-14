package com.seravian.feat_chat.domain.repository

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_chat.data.dto.request.diagnosis.ChatDiagnosesRequest
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosesDeletionRequest
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosisDeletionRequest
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosisDetailsRequest
import com.seravian.core_chat.domain.models.Diagnosis
import kotlinx.coroutines.flow.Flow

interface DiagnosisRepository {
    fun getDiagnoses(
        chatDiagnosesRequest: ChatDiagnosesRequest
    ): Flow<NetworkResult<List<Diagnosis>, NetworkError>>

    suspend fun getDiagnosis(
        diagnosisDetailsRequest: DiagnosisDetailsRequest
    ): NetworkResult<Diagnosis, NetworkError>

    suspend fun deleteDiagnosis(
        diagnosisDeletionRequest: DiagnosisDeletionRequest
    ): EmptyResult<NetworkError>

    suspend fun deleteDiagnoses(
        diagnosesDeletionRequest: DiagnosesDeletionRequest
    ): EmptyResult<NetworkError>
}