package com.seravian.feat_profile.domain.repository

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_tokens.domain.Tokens
import com.seravian.core_profile.domain.Profile

interface ProfileRepository {
    fun isCurrentThemeDark(): Boolean
    fun isCurrentLanguageArabic(): Boolean
    suspend fun changeTheme(isDarkTheme: Boolean)
    fun changeLanguage(languageTag: String)
    suspend fun getProfile(): Profile
    suspend fun logoutUser(): EmptyResult<NetworkError>
}