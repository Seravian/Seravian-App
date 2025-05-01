package com.seravian.feat_chat.domain

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_chat.data.dto.request.CreateChatRequest
import com.seravian.core_chat.data.dto.request.DeleteChatRequest
import com.seravian.core_chat.data.dto.request.EditChatRequest
import com.seravian.core_chat.data.dto.respose.CreateChatResponse
import com.seravian.core_chat.data.dto.respose.EditChatResponse

interface ChatRepository {
    suspend fun createChat(createChatRequest: CreateChatRequest):NetworkResult<CreateChatResponse,NetworkError>

    suspend fun updateChat(editChatRequest: EditChatRequest):NetworkResult<EditChatResponse,NetworkError>

    suspend fun deleteChat(deleteChatRequest: DeleteChatRequest):EmptyResult<NetworkError>
}