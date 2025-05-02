package com.seravian.feat_chat.domain

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_chat.data.dto.request.CreateChatRequest
import com.seravian.core_chat.data.dto.request.DeleteChatRequest
import com.seravian.core_chat.data.dto.request.EditChatRequest
import com.seravian.core_chat.data.dto.request.GetChatMessagesRequest
import com.seravian.core_chat.data.dto.respose.ChatMessagesResponse
import com.seravian.core_chat.data.dto.respose.ChatResponse
import com.seravian.core_chat.data.dto.respose.CreateChatResponse
import com.seravian.core_chat.data.dto.respose.EditChatResponse
import com.seravian.core_chat.domain.models.Chat
import com.seravian.core_chat.domain.models.Message

interface ChatRepository {
    suspend fun createChat(createChatRequest: CreateChatRequest): NetworkResult<Chat,NetworkError>

    suspend fun updateChat(editChatRequest: EditChatRequest): NetworkResult<Chat,NetworkError>

    suspend fun deleteChat(deleteChatRequest: DeleteChatRequest): EmptyResult<NetworkError>

    suspend fun getChats(): NetworkResult<List<Chat>, NetworkError>

    suspend fun getChatMessages(
        getChatMessagesRequest: GetChatMessagesRequest
    ): NetworkResult<Pair<Chat, List<Message>>, NetworkError>
}