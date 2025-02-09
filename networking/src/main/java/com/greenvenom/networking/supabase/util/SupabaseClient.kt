package com.greenvenom.networking.supabase.util

import com.greenvenom.networking.BuildConfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.coil.Coil3Integration
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

class SupabaseClient {
    private val client = createSupabaseClient(
        supabaseUrl = BuildConfig.BASE_URL,
        supabaseKey = BuildConfig.API_KEY,
    ) {
        install(Postgrest)
        install(Auth)
        install(Storage)
        install(Coil3Integration)
    }

    fun getClient() = client
}