package com.greenvenom.networking.supabase.data

import com.greenvenom.networking.domain.NetworkError

data class SupabaseError(
    override val message: String = "",
    val description: String? = null,
): NetworkError()