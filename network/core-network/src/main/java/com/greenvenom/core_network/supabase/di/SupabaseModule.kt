package com.greenvenom.core_network.supabase.di

import com.greenvenom.core_network.supabase.util.SupabaseClient
import org.koin.dsl.module

val supabaseModule = module {
    single<SupabaseClient> { SupabaseClient() }
}