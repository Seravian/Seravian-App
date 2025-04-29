package com.seravian.feat_network.di

import com.seravian.feat_network.data.SeravianDataSource
import com.greenvenom.core_network.domain.repository.RemoteDataSource
import com.greenvenom.core_network.domain.repository.SessionRepository
import com.greenvenom.core_tokens.domain.repo.TokenDataSource
import com.seravian.feat_network.data.repository.SeravianSessionRepository
import com.seravian.feat_network.util.HttpClientFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient>(qualifier = named("publicClient")) {
        HttpClientFactory.publicClient(
            engine = CIO.create()
        )
    }

    single<HttpClient>(qualifier = named("authorizedClient")) {
        HttpClientFactory.authorizedClient(
            engine = CIO.create(),
            tokensDataSource = get()
        )
    }

    single<RemoteDataSource> {
        SeravianDataSource(
            publicHttpClient = get(named("publicClient")),
            authorizedHttpClient = get(named("authorizedClient"))
        )
    }

    single<SessionRepository> {
        SeravianSessionRepository(
            tokenDataSource = get(),
            remoteDataSource = get()
        )
    }
}
