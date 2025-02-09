package com.greenvenom.ui.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

open class BaseViewModel: ViewModel() {
    var baseState = MutableStateFlow(BaseState())
        private set

    fun baseAction(action: BaseAction) {
        when(action) {
            is BaseAction.ShowLoading -> showLoading()
            is BaseAction.HideLoading -> hideLoading()
            is BaseAction.ShowErrorMessage -> showErrorMessage(action.errorMessage)
            is BaseAction.HideErrorMessage -> hideErrorMessage()
            else -> Unit
        }
    }

    fun showLoading() {
        baseState.value = baseState.value.copy(isLoading = true)
    }

    fun hideLoading() {
        baseState.value = baseState.value.copy(isLoading = false)
    }

    fun showErrorMessage(givenMessage: String) {
        baseState.value = baseState.value.copy(errorMessage = givenMessage)
    }

    fun hideErrorMessage() {
        baseState.value = baseState.value.copy(errorMessage = "")
    }
}