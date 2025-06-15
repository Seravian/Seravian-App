package com.seravian.feat_profile.presentation.viewModel

import androidx.lifecycle.viewModelScope
import com.seravian.core_ui.presentation.BaseViewModel
import com.seravian.feat_profile.domain.repository.ProfileRepository
import com.seravian.feat_profile.presentation.ProfileAction
import com.seravian.feat_profile.presentation.ProfileState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository
): BaseViewModel() {
    private val _profileState = MutableStateFlow(ProfileState())
    val profileState = _profileState.asStateFlow()

    fun profileAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.LoadInfo -> loadInfo()
            is ProfileAction.UpdateTheme -> updateTheme(action.isDark)
            is ProfileAction.UpdateLanguage -> updateLanguage(action.languageTag)
            is ProfileAction.Logout -> logout()
            is ProfileAction.ClearState -> clearState()
            ProfileAction.GoToVerificationRequests -> {}
        }
    }

    private fun loadInfo() {
        viewModelScope.launch {
            _profileState.update {
                it.copy(
                    profile = profileRepository.getProfile(),
                    isDarkTheme = profileRepository.isCurrentThemeDark(),
                    currentLanguage = if (profileRepository.isCurrentLanguageArabic()) "ar" else "en"
                )
            }
        }
    }

    private fun updateTheme(isDark: Boolean) {
        viewModelScope.launch {
            profileRepository.changeTheme(isDark)
            _profileState.update {
                it.copy(isDarkTheme = isDark)
            }
        }
    }

    private fun updateLanguage(languageTag: String) {
        profileRepository.changeLanguage(languageTag)
        _profileState.update {
            it.copy(currentLanguage = languageTag)
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _profileState.update {
                it.copy(
                    logoutResult = profileRepository.logoutUser()
                )
            }
        }
    }

    private fun clearState() {
        _profileState.value = ProfileState()
    }
}
