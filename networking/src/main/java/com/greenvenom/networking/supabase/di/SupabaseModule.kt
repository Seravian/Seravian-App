package com.greenvenom.networking.supabase.di

import com.greenvenom.networking.domain.repository.SessionStateRepository
import com.greenvenom.networking.supabase.data.repository.SupabaseSessionRepository
import com.greenvenom.networking.supabase.util.SupabaseClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val supabaseModule = module {
    single<SupabaseClient> { SupabaseClient() }
    single<SessionStateRepository>(qualifier = named<SupabaseSessionRepository>()) {
        SupabaseSessionRepository(supabaseClient = get())
    }
}