package com.seravian.seravianapp.features.auth.presentation.login

import com.seravian.domain.auth.CredentialState

data class LoginInputState(
    val emailValidity: CredentialState? = null,
    val passwordValidity: CredentialState? = null,
)