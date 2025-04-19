package com.seravian.feat_network.di

import com.greenvenom.core_auth.domain.repository.AuthRepository
import com.greenvenom.core_network.domain.TokenRepository
import com.greenvenom.core_onboarding.domain.OnBoardingRepository
import com.seravian.feat_network.data.features.auth.AuthRepositoryImpl
import com.seravian.feat_network.data.features.onboarding.OnBoardingRepositoryImpl
import com.seravian.feat_network.data.local.EncryptedTokenDataSource
import com.seravian.feat_network.data.local.TokenRepositoryImpl
import com.seravian.feat_network.data.remote.SeravianDataSource
import com.seravian.feat_network.domain.local.TokenDataSource
import com.seravian.feat_network.domain.remote.RemoteDataSource
import com.seravian.feat_network.util.HttpClientFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkFeatureModule = module {

    single<TokenDataSource>(named<EncryptedTokenDataSource>()) {
        EncryptedTokenDataSource(context = androidContext())
    }

    single<TokenRepository> {
        TokenRepositoryImpl(
            remoteDataSource = get(),
            tokenDataSource = get(named<EncryptedTokenDataSource>())
        )
    }

    single<HttpClient>(qualifier = named("publicClient")) {
        HttpClientFactory.publicClient(
            engine = CIO.create()
        )
    }

    single<HttpClient>(qualifier = named("authorizedClient")) {
        HttpClientFactory.authorizedClient(
            engine = CIO.create(),
            tokenRepo = get<TokenRepository>()
        )
    }

    single<RemoteDataSource> {
        SeravianDataSource(
            publicHttpClient = get(named("publicClient")),
            authorizedHttpClient = get(named("authorizedClient"))
        )
    }

    single<AuthRepository> {
        AuthRepositoryImpl(remoteDataSource = get())
    }

    single<OnBoardingRepository> {
        OnBoardingRepositoryImpl(remoteDataSource = get())
    }
}
