package com.seravian.feat_local.di

import com.seravian.core_local.domain.TokenDataSource
import com.seravian.feat_local.data.EncryptedTokenDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localFeatureModule = module {
    single<TokenDataSource> {
        EncryptedTokenDataSource(context = androidContext())
    }
}