package com.greenvenom.feat_tokens.di

import com.greenvenom.core_network.domain.repository.TokensRepository
import com.greenvenom.core_tokens.domain.repo.TokenDataSource
import com.greenvenom.feat_tokens.data.EncryptedTokenDataSource
import com.greenvenom.feat_tokens.data.TokensRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val tokensModule = module {
    single<TokenDataSource> {
        EncryptedTokenDataSource(context = androidContext())
    }

    single<TokensRepository> {
        TokensRepositoryImpl(
            tokenDataSource = get(),
            publicHttpClient = get(named("publicClient")),
        )
    }
}