package com.greenvenom.ui.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel: ViewModel() {
    var baseState = MutableStateFlow(BaseState())
        private set

    fun baseAction(action: BaseAction) {
        when(action) {
            is BaseAction.ShowLoading -> showLoading()
            is BaseAction.HideLoading -> hideLoading()
            is BaseAction.ShowErrorMessage -> showErrorMessage(action.errorMessage)
            is BaseAction.HideErrorMessage -> hideErrorMessage()
        }
    }

    fun showLoading() {
        baseState.update { it.copy(isLoading = true) }
    }

    fun hideLoading() {
        baseState.update { it.copy(isLoading = false) }
    }

    fun showErrorMessage(givenMessage: String) {
        baseState.update { it.copy(errorMessage = givenMessage) }
    }

    fun hideErrorMessage() {
        baseState.update { it.copy(errorMessage = "") }
    }
}