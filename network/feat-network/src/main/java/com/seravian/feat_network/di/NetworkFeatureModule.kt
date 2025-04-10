package com.seravian.feat_network.di

import com.greenvenom.core_auth.domain.repository.AuthRepository
import com.greenvenom.core_network.domain.TokenRepository
import com.greenvenom.core_onboarding.domain.OnBoardingRepository
import com.seravian.feat_network.data.features.auth.AuthRepositoryImpl
import com.seravian.feat_network.data.features.onboarding.OnBoardingRepositoryImpl
import com.seravian.feat_network.data.local.TokenDataSource
import com.seravian.feat_network.data.local.TokenRepositoryImpl
import com.seravian.feat_network.data.remote.SeravianDataSource
import com.seravian.feat_network.domain.local.LocalDataSource
import com.seravian.feat_network.domain.remote.RemoteDataSource
import com.seravian.feat_network.util.HttpClientFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
//
//val networkFeatureModule = module {
//    single<AuthRepository> {
//        AuthRepositoryImpl(remoteDataSource = get())
//    }
//
//    single<OnBoardingRepository> {
//        OnBoardingRepositoryImpl(remoteDataSource = get())
//    }
//
//    single<RemoteDataSource> {
//        SeravianDataSource(
//            publicHttpClient = get(named("publicClient")),
//            authorizedHttpClient = get(named("authorizedClient"))
//        )
//    }
//
//    single<TokenRepository> {
//        TokenRepositoryImpl(
//            remoteDataSource = get(),
//            localDataSource = get(named<TokenDataSource>())
//        )
//    }
//
//    single<LocalDataSource>(named<TokenDataSource>()) {
//        TokenDataSource(context = androidContext())
//    }
//
//    single<HttpClient>(qualifier = named("publicClient")) {
//        HttpClientFactory.publicClient(CIO.create())
//    }
//
//    single<HttpClient>(qualifier = named("authorizedClient")) {
//        HttpClientFactory.authorizedClient(CIO.create())
//    }
//}
val networkFeatureModule = module {

    single<LocalDataSource>(named<TokenDataSource>()) {
        TokenDataSource(context = androidContext())
    }

    single<TokenRepository> {
        TokenRepositoryImpl(
            remoteDataSource = get(), // make sure this doesn't trigger authorizedClient directly
            localDataSource = get(named<TokenDataSource>())
        )
    }

    single<HttpClient>(qualifier = named("publicClient")) {
        HttpClientFactory.publicClient(CIO.create())
    }

    single<HttpClient>(qualifier = named("authorizedClient")) {
        HttpClientFactory.authorizedClient(CIO.create()) {
            get<TokenRepository>() // injected only when needed
        }
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
