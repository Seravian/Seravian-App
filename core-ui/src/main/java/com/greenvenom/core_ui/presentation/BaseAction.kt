package com.greenvenom.core_ui.presentation

sealed interface BaseAction {
    data object ShowLoading: BaseAction
    data object HideLoading: BaseAction
    data class ShowErrorMessage(
        val errorMessage: String,
        val dismissAction: (() -> Unit)? = null
    ): BaseAction
    data object HideErrorMessage: BaseAction
}