package com.seravian.feat_chat.data.repository

import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.map
import com.greenvenom.core_network.data.onError
import com.greenvenom.core_network.data.onSuccess
import com.seravian.core_chat.data.dto.request.diagnosis.ChatDiagnosesRequest
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosisDetailsRequest
import com.seravian.core_chat.domain.models.Diagnosis
import com.seravian.core_local.domain.LocalDataSource
import com.seravian.feat_chat.domain.ChatBotRemoteDataSource
import com.seravian.feat_chat.domain.repository.DiagnosisRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DiagnosisRepositoryImpl(
    private val remoteDataSource: ChatBotRemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val chatBotStateRepository: ChatBotStateRepository
): DiagnosisRepository {
    var currentChatId: String = ""

    override fun getDiagnoses(
        chatDiagnosesRequest: ChatDiagnosesRequest
    ): Flow<NetworkResult<List<Diagnosis>, NetworkError>> = channelFlow {
        currentChatId = chatDiagnosesRequest.chatId
        var lastNotifiedDiagnosisId: Long = -1
        val diagnosesList = mutableListOf<Diagnosis>()

        localDataSource.getChatDiagnoses(currentChatId)
            .onEach { diagnoses ->
                send(NetworkResult.Success(
                    diagnoses.map { it.extractDiagnosis() }
                ))
            }
            .launchIn(this)

        diagnosesList.addAll(
            localDataSource.getChatDiagnoses(currentChatId)
                .first().map { it.extractDiagnosis() }
        )

        remoteDataSource.getChatDiagnoses(chatDiagnosesRequest)
            .map { response -> response.map { it.extractDiagnosis() } }
            .onSuccess { diagnoses ->
                localDataSource.insertDiagnoses(
                    diagnoses.map { it.toEntity(currentChatId) }
                )
            }
            .onError { error ->
                send(NetworkResult.Error(error))
            }

        chatBotStateRepository.chatBotState
            .onEach { state ->
                if (state.lastNotifiedDiagnosisId != null && state.lastNotifiedDiagnosisId != lastNotifiedDiagnosisId) {
                    getDiagnosis(DiagnosisDetailsRequest(state.lastNotifiedDiagnosisId))
                }
            }
            .launchIn(this)
    }

    override suspend fun getDiagnosis(
        diagnosisDetailsRequest: DiagnosisDetailsRequest
    ): NetworkResult<Diagnosis, NetworkError> {
        return remoteDataSource.getDiagnosisDetails(diagnosisDetailsRequest)
            .map { it.extractDiagnosis() }
            .onSuccess { localDataSource.insertDiagnosis(it.toEntity(currentChatId)) }
    }
}