package com.seravian.feat_profile.data.repository

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_tokens.domain.Tokens
import com.greenvenom.core_tokens.domain.repo.TokensDataSource
import com.seravian.core_local.domain.LocalDataSource
import com.seravian.core_local.domain.PrefsDataSource
import com.seravian.core_profile.data.local.extractProfile
import com.seravian.core_profile.data.remote.request.LogoutRequest
import com.seravian.core_profile.domain.Profile
import com.seravian.feat_profile.domain.ProfileRemoteDataSource
import com.seravian.feat_profile.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val profileDataSource: ProfileRemoteDataSource,
    private val appPrefsDataSource: PrefsDataSource,
    private val roomDataSource: LocalDataSource,
    private val tokensDataSource: TokensDataSource
): ProfileRepository {
    override fun isCurrentThemeDark(): Boolean {
        return appPrefsDataSource.appPrefsState.value.isDarkTheme
    }

    override fun isCurrentLanguageArabic(): Boolean {
        return appPrefsDataSource.appPrefsState.value.currentLanguageTag == "ar"
    }

    override suspend fun changeTheme( isDarkTheme: Boolean) {
        appPrefsDataSource.changeTheme(
            isDarkTheme = isDarkTheme
        )
    }

    override fun changeLanguage(languageTag: String) {
        appPrefsDataSource.changeLanguage(languageTag)
    }

    override suspend fun getLocalProfile(): Profile {
        return roomDataSource.getProfile().extractProfile()
    }

    override suspend fun getStoredTokens(): Tokens {
        return tokensDataSource.getStoredTokens()
    }

    override suspend fun logoutUser(): EmptyResult<NetworkError> {
        return profileDataSource.logoutUser(LogoutRequest(getStoredTokens().refreshToken ?: ""))
            .onSuccess {
                roomDataSource.deleteProfile()
                tokensDataSource.deleteTokens()
            }
    }
}

