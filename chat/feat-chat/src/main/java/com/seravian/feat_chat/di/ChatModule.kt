package com.seravian.feat_chat.di

import com.seravian.feat_chat.data.SeravianChatDataSource
import com.seravian.feat_chat.data.repository.ChatRepositoryImpl
import com.seravian.feat_chat.domain.ChatRemoteDataSource
import com.seravian.feat_chat.domain.ChatRepository
import com.seravian.feat_chat.presentation.viewModel.ChatViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val chatModule = module {
    single<ChatRemoteDataSource> {
        SeravianChatDataSource(
            authorizedHttpClient = get(named("authorizedClient")),
            signalRConnection = get()
        )
    }

    single<ChatRepository> {
        ChatRepositoryImpl(
            chatDataSource = get(),
            roomDataSource = get()
        )
    }

    viewModel {
        ChatViewModel(
            chatRepository = get()
        )
    }
}