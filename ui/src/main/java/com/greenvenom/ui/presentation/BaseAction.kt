package com.greenvenom.ui.presentation

sealed interface BaseAction {
    data object ShowLoading: BaseAction
    data object HideLoading: BaseAction
    data class ShowErrorMessage(val errorMessage: String): BaseAction
    data object HideErrorMessage: BaseAction
}