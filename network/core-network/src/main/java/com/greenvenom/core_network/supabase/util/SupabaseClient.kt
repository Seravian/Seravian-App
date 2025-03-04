package com.greenvenom.core_network.supabase.util

import com.greenvenom.core_network.BuildConfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.coil.Coil3Integration
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

@OptIn(ExperimentalSerializationApi::class)
class SupabaseClient {
    private val client = createSupabaseClient(
        supabaseUrl = BuildConfig.BASE_URL,
        supabaseKey = BuildConfig.API_KEY,
    ) {
        install(Postgrest)
        install(Auth)
        install(Storage)
        install(Coil3Integration)
        defaultSerializer = KotlinXSerializer(Json {
            ignoreUnknownKeys = true
            namingStrategy = JsonNamingStrategy.SnakeCase
        })
    }

    fun getClient() = client
}