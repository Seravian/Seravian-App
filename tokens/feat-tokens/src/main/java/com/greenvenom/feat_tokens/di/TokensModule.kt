package com.greenvenom.feat_tokens.di

import com.greenvenom.core_tokens.domain.repo.TokensDataSource
import com.greenvenom.feat_tokens.data.EncryptedTokensDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val tokensModule = module {
    single<TokensDataSource> {
        EncryptedTokensDataSource(
            publicHttpClient = get(named("publicClient")),
            context = androidContext()
        )
    }
}