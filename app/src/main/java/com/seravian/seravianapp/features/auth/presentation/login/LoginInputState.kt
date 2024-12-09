package com.seravian.seravianapp.features.auth.presentation.login

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.seravian.domain.auth.CredentialState

@Immutable
data class LoginInputState(
    val emailValidity: CredentialState? = null,
    val passwordValidity: CredentialState? = null,
)