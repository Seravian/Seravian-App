package com.seravian.auth.presentation

sealed interface AccountAction {
    data object Logout: AccountAction
}