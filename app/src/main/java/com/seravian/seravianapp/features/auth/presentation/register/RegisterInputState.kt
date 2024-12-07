package com.seravian.seravianapp.features.auth.presentation.register

import com.seravian.domain.auth.CredentialState

data class RegisterInputState(
    val emailValidity: CredentialState? = null,
    val usernameValidity: CredentialState? = null,
    val passwordValidity: CredentialState? = null,
    val confirmPasswordValidity: CredentialState? = null
)