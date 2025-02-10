package com.greenvenom.networking.supabase.data

import com.greenvenom.networking.domain.Error

data class SupabaseError(
    override val message: String = "",
    val description: String? = null,
): Error()