package com.seravian.feat_chat.di

import com.seravian.feat_chat.data.SeravianChatBotDataSource
import com.seravian.feat_chat.data.repository.ChatBotStateRepository
import com.seravian.feat_chat.data.repository.ChatRepositoryImpl
import com.seravian.feat_chat.data.repository.ChatsListRepositoryImpl
import com.seravian.feat_chat.data.repository.VoiceModeRepositoryImpl
import com.seravian.feat_chat.domain.ChatBotRemoteDataSource
import com.seravian.feat_chat.domain.repository.ChatRepository
import com.seravian.feat_chat.domain.repository.ChatsListRepository
import com.seravian.feat_chat.domain.repository.VoiceModeRepository
import com.seravian.feat_chat.presentation.viewModel.chat.ChatViewModel
import com.seravian.feat_chat.presentation.viewModel.chats_list.ChatsListViewModel
import com.seravian.feat_chat.presentation.viewModel.voice.VoiceModeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val chatModule = module {
    single<ChatBotRemoteDataSource> {
        SeravianChatBotDataSource(
            authorizedHttpClient = get(named("authorizedClient")),
            signalRConnection = get()
        )
    }

    single {
        ChatBotStateRepository(
            seravianChatBotDataSource = get()
        )
    }

    single<ChatsListRepository> {
        ChatsListRepositoryImpl(
            seravianChatBotDataSource = get(),
            roomDataSource = get()
        )
    }

    single<ChatRepository> {
        ChatRepositoryImpl(
            seravianChatBotDataSource = get(),
            roomDataSource = get()
        )
    }

    single<VoiceModeRepository> {
        VoiceModeRepositoryImpl(
            seravianChatBotDataSource = get(),
        )
    }

    viewModel {
        ChatsListViewModel(
            chatsListRepository = get(),
            chatBotStateRepository = get(),
        )
    }

    viewModel {
        ChatViewModel(
            chatRepository = get(),
            chatBotStateRepository = get(),
        )
    }

    viewModel {
        VoiceModeViewModel(
            voiceModeRepository = get(),
            chatBotStateRepository = get(),
        )
    }
}