package com.seravian.data.datasource.di

import com.greenvenom.networking.domain.datasource.RemoteDataSource
import com.seravian.data.datasource.remote.SeravianDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataSourceModule = module {
    single<RemoteDataSource>(qualifier = named<SeravianDataSource>()) {
        SeravianDataSource(
            httpClient = get()
        )
    }

    single<RemoteDataSource> {
        get(qualifier = named<SeravianDataSource>())
    }
}