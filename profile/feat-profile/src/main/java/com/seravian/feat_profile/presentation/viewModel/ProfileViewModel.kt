package com.seravian.feat_profile.presentation.viewModel

import com.greenvenom.core_ui.presentation.BaseViewModel
import com.seravian.feat_profile.domain.ProfileRepository
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.seravian.feat_profile.presentation.model.ProfileUI
import com.seravian.feat_profile.presentation.model.toProfileUI
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : BaseViewModel() {

    val profileUI = mutableStateOf<ProfileUI?>(null)
    val isDarkTheme = mutableStateOf(false)
    val currentLanguage = mutableStateOf("en")

    init {
        loadUserData()
        loadSettings()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                val profile = profileRepository.getLocalProfile()
                profileUI.value = profile.toProfileUI()
            } catch (e: Exception) {
                Log.e("TAG", "loadUserData: Error to get user data", )
            }
        }
    }

    private fun loadSettings() {
        isDarkTheme.value = profileRepository.isCurrentThemeDark()
        currentLanguage.value = if (profileRepository.isCurrentLanguageArabic()) "ar" else "en"
    }

    fun updateTheme( isDark: Boolean) {
        viewModelScope.launch {
            profileRepository.changeTheme( isDark)
            isDarkTheme.value = isDark
        }
    }

    // Update language
    fun updateLanguage(languageTag: String) {
        profileRepository.changeLanguage(languageTag)
        currentLanguage.value = languageTag
    }

    fun logout( onComplete: () -> Unit) {
        viewModelScope.launch {
            profileRepository.logoutUser()
            onComplete()
        }
    }


}
