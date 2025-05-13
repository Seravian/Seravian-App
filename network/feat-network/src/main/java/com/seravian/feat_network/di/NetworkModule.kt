package com.seravian.feat_network.di

import com.seravian.feat_network.data.SeravianDataSource
import com.greenvenom.core_network.domain.repository.RemoteDataSource
import com.greenvenom.core_network.domain.repository.SessionRepository
import com.seravian.feat_network.data.SignalRConnection
import com.seravian.feat_network.data.repository.SeravianSessionRepository
import com.seravian.feat_network.util.ClientFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient>(qualifier = named("publicClient")) {
        ClientFactory.publicClient(
            engine = CIO.create()
        )
    }

    single<HttpClient>(qualifier = named("authorizedClient")) {
        ClientFactory.authorizedClient(
            engine = CIO.create(),
            tokensDataSource = get()
        )
    }

    single<SignalRConnection> {
        SignalRConnection(
            tokensDataSource = get(),
            httpClient = get(named("publicClient"))
        )
    }

    single<RemoteDataSource> {
        SeravianDataSource(
            publicHttpClient = get(named("publicClient")),
            authorizedHttpClient = get(named("authorizedClient")),
            signalRConnection = get()
        )
    }

    single<SessionRepository> {
        SeravianSessionRepository(
            tokenDataSource = get(),
            remoteDataSource = get()
        )
    }
}
