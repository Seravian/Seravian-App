package com.greenvenom.auth.presentation.login

sealed interface LoginAction {
    data class ValidateEmail(val email: String): LoginAction
    data class ValidatePassword(val password: String): LoginAction
    data class Login(
        val email: String,
        val password: String,
    ): LoginAction
    data object ResetState: LoginAction
    data object ResetNetworkResult: LoginAction
}