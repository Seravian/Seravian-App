package com.seravian.core_local.domain

import android.content.Context
import com.seravian.core_local.data.AppPrefsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PrefsDataSource {
    val appPrefsState: StateFlow<AppPrefsState>

    suspend fun changeTheme(isDarkTheme: Boolean)
    fun getThemePreference(): Flow<Boolean>
    fun changeLanguage(languageTag: String)
    fun getCurrentLanguage(): String
}