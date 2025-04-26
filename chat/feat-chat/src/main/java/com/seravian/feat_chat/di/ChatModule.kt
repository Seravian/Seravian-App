package com.seravian.feat_chat.di

import com.seravian.feat_chat.presentation.viewModel.ChatViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val chatModule = module {
    viewModel { ChatViewModel() }
}