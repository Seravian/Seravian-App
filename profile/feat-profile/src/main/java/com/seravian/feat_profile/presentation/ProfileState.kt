package com.seravian.feat_profile.presentation

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.seravian.core_profile.domain.Profile

data class ProfileState(
    val profile: Profile? = null,
    val isDarkTheme: Boolean = false,
    val currentLanguage: String = "en",
    val logoutResult: EmptyResult<NetworkError>? = null
)
