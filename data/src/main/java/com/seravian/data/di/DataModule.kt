package com.seravian.data.di

import com.seravian.data.utils.SessionDestinationHandler
import org.koin.dsl.module

val dataModule = module {
    single {
        SessionDestinationHandler(
            navigationStateRepository = get(),
            sessionStateRepository = get()
        )
    }
}