package com.seravian.data.datasource.di

import com.seravian.data.datasource.remote.SeravianDataSource
import com.seravian.domain.datasource.RemoteDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataSourceModule = module {
    single<RemoteDataSource>(qualifier = named("seravian")) {
        SeravianDataSource()
    }

    single<RemoteDataSource> {
        get(qualifier = named("seravian"))
    }
}