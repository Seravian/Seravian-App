package com.seravian.seravianapp.di

import com.seravian.seravianapp.navigation.utils.SessionDestinationHandler
import org.koin.dsl.module

val appModule = module {
    single {
        SessionDestinationHandler(
            navigationStateRepository = get(),
            seravianSessionRepository = get()
        )
    }
}