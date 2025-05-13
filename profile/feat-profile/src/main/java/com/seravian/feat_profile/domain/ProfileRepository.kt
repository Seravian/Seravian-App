package com.seravian.feat_profile.domain

import android.content.Context
import com.greenvenom.core_tokens.domain.Tokens
import com.seravian.core_profile.domain.Profile

interface ProfileRepository {
    fun isCurrentThemeDark(): Boolean
    fun isCurrentLanguageArabic(): Boolean
    suspend fun changeTheme( isDarkTheme: Boolean)
    fun changeLanguage(languageTag: String)
    suspend fun getLocalProfile(): Profile
    suspend fun getStoredTokens(): Tokens?
    suspend fun logoutUser()

}