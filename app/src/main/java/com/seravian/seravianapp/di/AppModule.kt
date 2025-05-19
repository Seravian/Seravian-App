package com.seravian.seravianapp.di

import com.greenvenom.core_network.domain.repository.SessionRepository
import com.seravian.seravianapp.navigation.utils.SeravianSessionRepository
import com.seravian.seravianapp.navigation.utils.SessionDestinationHandler
import org.koin.dsl.module

val appModule = module {
    single<SessionRepository>(createdAtStart = true) {
        SeravianSessionRepository(
            tokensDataSource = get()
        )
    }

    single {
        SessionDestinationHandler(
            navigationStateRepository = get(),
            seravianSessionRepository = get()
        )
    }
}