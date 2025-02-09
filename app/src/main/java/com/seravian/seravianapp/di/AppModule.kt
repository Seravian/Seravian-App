package com.seravian.seravianapp.di

import com.seravian.data.utils.SessionDestinationHandler
import org.koin.dsl.module

val appModule = module {
    single {
        SessionDestinationHandler(
            navigationStateRepository = get(),
            sessionStateRepository = get()
        )
    }
}