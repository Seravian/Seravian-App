package com.seravian.feat_network.di

import com.greenvenom.core_auth.domain.repository.AuthRepository
import com.seravian.feat_network.data.features.auth.AuthRepositoryImpl
import com.seravian.feat_network.data.SeravianDataSource
import com.greenvenom.core_network.domain.RemoteDataSource
import com.greenvenom.core_tokens.domain.repo.TokensRepository
import com.seravian.feat_network.util.HttpClientFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkFeatureModule = module {
    single<HttpClient>(qualifier = named("publicClient")) {
        HttpClientFactory.publicClient(
            engine = CIO.create()
        )
    }

    single<HttpClient>(qualifier = named("authorizedClient")) {
        HttpClientFactory.authorizedClient(
            engine = CIO.create(),
            tokensRepo = get<TokensRepository>()
        )
    }

    single<RemoteDataSource> {
        SeravianDataSource(
            publicHttpClient = get(named("publicClient")),
            authorizedHttpClient = get(named("authorizedClient"))
        )
    }

    single<AuthRepository> {
        AuthRepositoryImpl(
            remoteDataSource = get(),
            roomDataSource = get(),
            tokenDataSource = get(),
            emailStateRepository = get()
        )
    }
}
