package com.seravian.feat_profile.domain

import android.content.Context

interface ProfileRepository {
    fun isCurrentThemeDark(): Boolean
    fun isCurrentLanguageArabic(): Boolean
    suspend fun changeTheme(context: Context, isDarkTheme: Boolean)
    fun changeLanguage(languageTag: String)
}