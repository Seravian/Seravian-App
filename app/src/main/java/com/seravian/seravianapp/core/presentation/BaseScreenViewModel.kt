package com.seravian.seravianapp.core.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

open class BaseScreenViewModel: ViewModel() {
    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState.asStateFlow()

    private val _dialogMessage = MutableStateFlow<String>("")
    val dialogMessage: StateFlow<String> = _dialogMessage.asStateFlow()

    fun showLoading() {
        _loadingState.value = true
    }

    fun hideLoading() {
        _loadingState.value = false
    }

    fun showErrorMessage(errorMessage: String) {
        _dialogMessage.value = errorMessage
    }

    fun hideErrorMessage() {
        _dialogMessage.value = ""
    }
}