package com.greenvenom.networking.di

import com.greenvenom.networking.domain.HttpClientFactory
import io.ktor.client.engine.cio.CIO
import org.koin.dsl.module

val networkingModule = module {
    single { HttpClientFactory.create(CIO.create()) }
}