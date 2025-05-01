package com.seravian.feat_chat.di

import com.seravian.feat_chat.data.repository.ChatRepositoryImpl
import com.seravian.feat_chat.domain.ChatRepository
import com.seravian.feat_chat.presentation.viewModel.ChatViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val chatModule = module {

    single<ChatRepository> {
        ChatRepositoryImpl(
            remoteDataSource = get()
        )
    }

    viewModel { ChatViewModel() }
}