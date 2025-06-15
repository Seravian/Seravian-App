package com.seravian.feat_chat.data.repository

import com.seravian.core_network.data.EmptyResult
import com.seravian.core_network.data.NetworkError
import com.seravian.core_network.data.NetworkResult
import com.seravian.core_network.data.map
import com.seravian.core_network.data.onError
import com.seravian.core_network.data.onSuccess
import com.seravian.core_chat.data.dto.request.diagnosis.ChatDiagnosesRequest
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosesDeletionRequest
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosisDeletionRequest
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

        // Emit initial local data
        localDataSource.getChatDiagnoses(currentChatId)
            .onEach { diagnoses ->
                val extractedDiagnoses = diagnoses.map { it.extractDiagnosis() }
                send(NetworkResult.Success(
                    extractedDiagnoses
                ))
            }
            .launchIn(this)

        // Get current local diagnoses for comparison
        diagnosesList.addAll(
            localDataSource.getChatDiagnoses(currentChatId)
                .first().map { it.extractDiagnosis() }
        )

        // Fetch from remote and sync with local
        remoteDataSource.getChatDiagnoses(chatDiagnosesRequest)
            .map { response -> response.map { it.extractDiagnosis() } }
            .onSuccess { remoteDiagnoses ->
                // Create sets of IDs for comparison
                val remoteIds = remoteDiagnoses.map { it.id }.toSet()
                val localIds = diagnosesList.map { it.id }.toSet()

                // Find diagnoses to delete (in local but not in remote)
                val diagnosesToDelete = localIds - remoteIds

                // Find diagnoses to insert/update (in remote but not in local, or different)
                val diagnosesToInsertOrUpdate = remoteDiagnoses.filter { remoteDiagnosis ->
                    val localDiagnosis = diagnosesList.find { it.id == remoteDiagnosis.id }
                    localDiagnosis == null || localDiagnosis.id != remoteDiagnosis.id
                }

                // Delete obsolete diagnoses
                if (diagnosesToDelete.isNotEmpty()) {
                    diagnosesToDelete.forEach {
                        localDataSource.deleteDiagnosis(it)
                    }
                }

                // Insert/update new or changed diagnoses
                if (diagnosesToInsertOrUpdate.isNotEmpty()) {
                    localDataSource.insertDiagnoses(
                        diagnosesToInsertOrUpdate.map { it.toEntity(currentChatId) }
                    )
                }
            }
            .onError { error ->
                send(NetworkResult.Error(error))
            }

        // Handle real-time diagnosis updates
        chatBotStateRepository.chatBotState
            .onEach { state ->
                if (state.lastNotifiedDiagnosisId != null && state.lastNotifiedDiagnosisId != lastNotifiedDiagnosisId) {
                    lastNotifiedDiagnosisId = state.lastNotifiedDiagnosisId
                    getDiagnosis(DiagnosisDetailsRequest(state.lastNotifiedDiagnosisId))
                }
            }
            .launchIn(this)
    }

    override suspend fun getDiagnosis(
        diagnosisDetailsRequest: DiagnosisDetailsRequest
    ): NetworkResult<Diagnosis, NetworkError> {
        val localDiagnosis = localDataSource.getChatDiagnosis(
            diagnosisDetailsRequest.chatDiagnosisId
        ).extractDiagnosis()

        return when {
            // Return local if diagnosedProblem and reasoning are both not null
            localDiagnosis.diagnosedProblem != null && localDiagnosis.reasoning != null -> {
                NetworkResult.Success(localDiagnosis)
            }
            // Return local if diagnosedProblem is null and failureReason is not null
            localDiagnosis.diagnosedProblem == null && localDiagnosis.failureReason != null -> {
                NetworkResult.Success(localDiagnosis)
            }
            // Return remote if diagnosedProblem is not null but reasoning is null
            // (or any other case not covered above)
            else -> {
                remoteDataSource.getDiagnosisDetails(diagnosisDetailsRequest)
                    .map { it.extractDiagnosis() }
                    .onSuccess { localDataSource.insertDiagnosis(it.toEntity(currentChatId)) }
            }
        }
    }

    override suspend fun deleteDiagnosis(
        diagnosisDeletionRequest: DiagnosisDeletionRequest
    ): EmptyResult<NetworkError> {
        return remoteDataSource.deleteDiagnosis(diagnosisDeletionRequest)
            .onSuccess { localDataSource.deleteDiagnosis(diagnosisDeletionRequest.chatDiagnosisId) }
    }

    override suspend fun deleteDiagnoses(
        diagnosesDeletionRequest: DiagnosesDeletionRequest
    ): EmptyResult<NetworkError> {
        return remoteDataSource.deleteDiagnoses(diagnosesDeletionRequest)
            .onSuccess { localDataSource.deleteDiagnoses(currentChatId) }
    }
}