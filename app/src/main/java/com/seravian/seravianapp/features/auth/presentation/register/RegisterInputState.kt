package com.seravian.seravianapp.features.auth.presentation.register

import androidx.compose.runtime.Immutable
import com.seravian.domain.auth.CredentialState

@Immutable
data class RegisterInputState(
    val emailValidity: CredentialState? = null,
    val usernameValidity: CredentialState? = null,
    val passwordValidity: CredentialState? = null,
    val confirmPasswordValidity: CredentialState? = null
)