package com.greenvenom.feat_tokens.di

import com.greenvenom.core_tokens.domain.repo.TokenDataSource
import com.greenvenom.feat_tokens.data.EncryptedTokenDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val tokensModule = module {
    single<TokenDataSource> {
        EncryptedTokenDataSource(context = androidContext())
    }
}