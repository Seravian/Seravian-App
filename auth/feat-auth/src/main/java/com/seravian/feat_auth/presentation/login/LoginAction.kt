package com.seravian.feat_auth.presentation.login

sealed interface LoginAction {
    data class ValidateEmail(val email: String): LoginAction
    data class ValidatePassword(val password: String): LoginAction
    data class Login(
        val email: String,
        val password: String,
    ): LoginAction
    data class StoreReceivedEmail(val email: String): LoginAction
    data object ResetState: LoginAction
    data object ResetNetworkResult: LoginAction
}