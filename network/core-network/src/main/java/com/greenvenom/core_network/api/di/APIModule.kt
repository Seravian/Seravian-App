package com.greenvenom.core_network.api.di

import com.greenvenom.core_network.api.utils.HttpClientFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.dsl.module

val apiModule = module {
    single<HttpClient> { HttpClientFactory.create(CIO.create()) }
}