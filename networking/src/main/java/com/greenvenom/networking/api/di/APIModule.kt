package com.greenvenom.networking.api.di

import com.greenvenom.networking.api.data.repository.APISessionRepository
import com.greenvenom.networking.api.utils.HttpClientFactory
import com.greenvenom.networking.domain.repository.SessionStateRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.core.qualifier.named
import org.koin.dsl.module

val apiModule = module {
    single<HttpClient> { HttpClientFactory.create(CIO.create()) }
    single<SessionStateRepository>(qualifier = named<APISessionRepository>()) {
        APISessionRepository(apiClient = get())
    }
}