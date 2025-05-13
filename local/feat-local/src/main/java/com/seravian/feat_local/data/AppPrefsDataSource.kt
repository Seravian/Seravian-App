package com.seravian.feat_local.data

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.seravian.core_local.data.AppPrefsState
import com.seravian.core_local.domain.PrefsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.Locale

class AppPrefsDataSource(private val context: Context): PrefsDataSource {
    private val _appPrefsState = MutableStateFlow(AppPrefsState())
    override val appPrefsState = _appPrefsState.asStateFlow()

    private val Context.themeDataStore by preferencesDataStore(name = "theme_prefs")
    private val darkThemeKey = booleanPreferencesKey(name = "dark_theme")

    init {
        _appPrefsState.update {
            it.copy(
                currentLanguageTag = getCurrentLanguage()
            )
        }
    }
    
    override suspend fun changeTheme( isDarkTheme: Boolean) {
        context.themeDataStore.edit { preferences ->
            preferences[darkThemeKey] = isDarkTheme
        }

        _appPrefsState.update {
            it.copy(
                isDarkTheme = isDarkTheme
            )
        }
    }

    override fun getThemePreference(): Flow<Boolean> =
        context.themeDataStore.data.map { preferences ->
            preferences[darkThemeKey] ?: false
        }

    override fun changeLanguage(languageTag: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageTag)
        AppCompatDelegate.setApplicationLocales(appLocale)

        _appPrefsState.update {
            it.copy(
                currentLanguageTag = languageTag
            )
        }
    }

    override fun getCurrentLanguage(): String {
        val locales: LocaleListCompat = AppCompatDelegate.getApplicationLocales()

        return if (locales.isEmpty) {
            Locale.ENGLISH.toLanguageTag()
        } else {
            locales[0]?.toLanguageTag() as String
        }
    }
}