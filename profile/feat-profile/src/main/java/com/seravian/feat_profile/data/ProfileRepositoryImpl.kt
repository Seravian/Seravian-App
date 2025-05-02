package com.seravian.feat_profile.data

import android.content.Context
import com.greenvenom.core_network.domain.repository.RemoteDataSource
import com.greenvenom.core_tokens.domain.repo.TokenDataSource
import com.seravian.core_local.domain.LocalDataSource
import com.seravian.core_local.domain.PrefsDataSource
import com.seravian.feat_profile.domain.ProfileRepository

class ProfileRepositoryImpl(
    private val appPrefsDataSource: PrefsDataSource,
    private val roomDataSource: LocalDataSource,
    private val seravianDataSource: RemoteDataSource,
    private val tokensDataSource: TokenDataSource
): ProfileRepository {
    override fun isCurrentThemeDark(): Boolean {
        return appPrefsDataSource.appPrefsState.value.isDarkTheme
    }

    override fun isCurrentLanguageArabic(): Boolean {
        return appPrefsDataSource.appPrefsState.value.currentLanguageTag == "ar"
    }

    override suspend fun changeTheme(context: Context, isDarkTheme: Boolean) {
        appPrefsDataSource.changeTheme(
            context = context,
            isDarkTheme = isDarkTheme
        )
    }

    override fun changeLanguage(languageTag: String) {
        appPrefsDataSource.changeLanguage(languageTag)
    }
}