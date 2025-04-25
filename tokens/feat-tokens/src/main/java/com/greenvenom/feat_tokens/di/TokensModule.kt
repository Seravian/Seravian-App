package com.greenvenom.feat_tokens.di

import com.greenvenom.core_tokens.domain.repo.TokensRepository
import com.greenvenom.feat_tokens.domain.TokenDataSource
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
            remoteDataSource = get(),
        )
    }
}