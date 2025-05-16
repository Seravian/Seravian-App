package com.seravian.feat_profile.presentation

sealed interface ProfileAction {
    data object LoadInfo : ProfileAction
    data class UpdateTheme(val isDark: Boolean) : ProfileAction
    data class UpdateLanguage(val languageTag: String) : ProfileAction
    data object Logout : ProfileAction
    data object ClearState : ProfileAction
}