package com.seravian.feat_tokens.di

import com.seravian.core_tokens.domain.repo.TokensDataSource
import com.seravian.feat_tokens.data.EncryptedTokensDataSource
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