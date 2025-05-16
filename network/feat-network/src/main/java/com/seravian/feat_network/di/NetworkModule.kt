package com.seravian.feat_network.di

import com.greenvenom.core_network.domain.RealtimeConnection
import com.seravian.feat_network.util.SignalRConnection
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

    single<RealtimeConnection> {
        SignalRConnection(
            tokensDataSource = get()
        )
    }
}
