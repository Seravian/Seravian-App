package com.seravian.domain.auth

sealed class CredentialState {
    object Valid: CredentialState()
    data class InValid(val message: String): CredentialState()
}