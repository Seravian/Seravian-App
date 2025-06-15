package com.seravian.feat_profile.domain.repository

import com.seravian.core_network.data.EmptyResult
import com.seravian.core_network.data.NetworkError
import com.seravian.core_profile.domain.Profile

interface ProfileRepository {
    fun isCurrentThemeDark(): Boolean

    fun isCurrentLanguageArabic(): Boolean

    suspend fun changeTheme(isDarkTheme: Boolean)

    fun changeLanguage(languageTag: String)

    suspend fun getProfile(isDoctor: Boolean = false): Profile

    suspend fun logoutUser(): EmptyResult<NetworkError>
}